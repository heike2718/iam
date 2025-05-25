// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.process;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.bv_admin.domain.Jobstatus;
import de.egladil.web.bv_admin.domain.mailversand.api.Mailversandgruppe;
import de.egladil.web.bv_admin.infrastructure.persistence.dao.MailversandDao;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenteMailversandgruppe;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUserReadOnly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * VersandgruppenSource stellt die Mailversandaufträge zur Abarbeitung zur Verfügung
 */
@ApplicationScoped
public class VersandgruppenSource {

	@Inject
	MailversandDao dao;

	/**
	 * Gibt die Mailversandgruppe in der Warteschlange zurück: die mit dem ältesten eingestellten Mailversandauftrag,
	 * der noch nicht beendet ist sortiert nach sortnr.
	 *
	 * @return Mailversandgruppe oder null
	 */
	public Mailversandgruppe getNextMailversandgruppe(final String idVersandauftrag) {

		List<PersistenteMailversandgruppe> gruppen = dao.findAllMailversandgruppenWithVersandauftragUUID(idVersandauftrag);

		PersistenteMailversandgruppe naechsteGruppe = getNext(gruppen);

		if (naechsteGruppe == null) {

			return null;
		}

		List<String> uuids = Arrays.asList(StringUtils.split(naechsteGruppe.getEmpfaengerUUIDs(), ","));

		List<PersistenterUserReadOnly> users = dao.findAktivierteUsersByUUIDs(uuids);
		List<String> emails = users.stream().map(u -> u.getEmail()).toList();

		Mailversandgruppe result = new Mailversandgruppe();
		result.setEmpfaengerEmails(emails);
		result.setEmpfaengerUUIDs(uuids);
		result.setSortnr(naechsteGruppe.getSortnr());
		result.setStatus(naechsteGruppe.getStatus());
		result.setUuid(naechsteGruppe.getUuid());
		result.setIdMailversandauftrag(idVersandauftrag);

		return result;
	}

	/**
	 * @param gruppen
	 * @return
	 */
	private PersistenteMailversandgruppe getNext(final List<PersistenteMailversandgruppe> gruppen) {

		Optional<PersistenteMailversandgruppe> optInProgress = gruppen.stream().filter(g -> Jobstatus.IN_PROGRESS == g.getStatus())
			.findFirst();

		if (optInProgress.isPresent()) {

			return null;
		}

		Optional<PersistenteMailversandgruppe> optWaiting = gruppen.stream().filter(g -> Jobstatus.WAITING == g.getStatus())
			.findFirst();

		return optWaiting.isEmpty() ? null : optWaiting.get();
	}

}
