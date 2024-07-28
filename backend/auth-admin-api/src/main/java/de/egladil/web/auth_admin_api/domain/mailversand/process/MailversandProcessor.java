// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand.process;

import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import de.egladil.web.auth_admin_api.domain.exceptions.MailversandException;
import de.egladil.web.auth_admin_api.domain.mailversand.api.AuthAdminMailDto;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailService;
import de.egladil.web.auth_admin_api.domain.mailversand.api.Mailversandgruppe;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.MailversandDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenteMailversandgruppe;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterMailversandauftrag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MailversandProcessor
 */
@ApplicationScoped
public class MailversandProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailversandProcessor.class);

	@Inject
	VersandgruppenSource versandgruppenSource;

	@Inject
	MailService mailService;

	@Inject
	MailversandDao dao;

	public void processMailversandauftrag() {

		LOGGER.info("pruefe Warteschlange");

		PersistenterMailversandauftrag persistenterMailversandAuftrag = dao.findOldestNotCompletedMailversandauftrag();

		if (persistenterMailversandAuftrag == null) {

			LOGGER.info("kein neuer Mailversandauftrag. Tue nix...");
			return;

		}

		Mailversandgruppe nextMailversandgruppe = versandgruppenSource
			.getNextMailversandgruppe(persistenterMailversandAuftrag.getUuid());

		if (nextMailversandgruppe == null) {

			if (!persistenterMailversandAuftrag.getStatus().isCompleted()) {

				// Es könnte sein, dass es gar keine Gruppe (mehr) gibt. Dann muss der Job auch beendet werden, da sonst ein Job
				// alle weiteren MAufträge blockiert

				// normalerweise wird bereits nach dem Versenden geschaut, ob anzahlEmpfaenger == anzahlVersendet und dann beendet,
				// aber zwischenzeitlich können sich USER in der DB verabschiedet haben und dann ist anzahlVersendet stets kleiner
				// als anzahlEmpfaenger. Für diesen Fall dauert es noch einen Jobzyklus, um den Versandauftrag als beendet zu
				// markieren
				markCompleted(persistenterMailversandAuftrag);
			}

			LOGGER.info("keine neue Mainversandgruppe. Tue nix...");
			return;
		}

		PersistenterInfomailTextReadOnly persistenterInfomailText = dao
			.findInfomailtextReadOnlyByID(persistenterMailversandAuftrag.getIdInfomailtext());

		if (persistenterInfomailText == null) {

			// Dieser Zweig ist nicht möglich wegen FK.
			LOGGER.error(
				"der Infomailtext {} ist verschwunden. Das ist nicht moeglich wegen FKs. Mailversandauftrag {} wird gelöscht",
				persistenterMailversandAuftrag.getIdInfomailtext(), persistenterMailversandAuftrag.getUuid());

			dao.removeMailversandauftrag(persistenterMailversandAuftrag);
			return;
		}

		if (persistenterMailversandAuftrag.getStatus() == Jobstatus.WAITING) {

			LOGGER.info("starte Mailversandauftrag {}: anzahl Empfänger={}", persistenterMailversandAuftrag.getUuid(),
				persistenterMailversandAuftrag.getAnzahlEmpfaenger());

			persistenterMailversandAuftrag = this.markStarded(persistenterMailversandAuftrag);

		}

		PersistenteMailversandgruppe persistenteMailversandGruppe = dao
			.findMailversandgruppeByUUID(nextMailversandgruppe.getUuid());

		long anzahlEmpfaenger = persistenterMailversandAuftrag.getAnzahlEmpfaenger();
		long anzahlVersendet = 0;

		try {

			if (!nextMailversandgruppe.getEmpfaengerEmails().isEmpty()) {

				sendeMails(persistenterInfomailText, nextMailversandgruppe);
			} else {

				LOGGER.info("Keine aktiven Mailadressen mehr in Mailversandgruppe {}", nextMailversandgruppe.getUuid());
			}
			persistenterMailversandAuftrag = updateMailversandauftragUndGruppe(persistenterMailversandAuftrag,
				persistenteMailversandGruppe,
				nextMailversandgruppe.getEmpfaengerEmails().size(), Jobstatus.COMPLETED);

			anzahlVersendet = persistenterMailversandAuftrag.getAnzahlVersendet();

		} catch (MailversandException e) {

			LOGGER.error("Beim Mailversand an Gruppe {} ist ein Fehler aufgetreten: {}", persistenteMailversandGruppe.getUuid(),
				e.getMessage(), e);
			persistenterMailversandAuftrag = updateMailversandauftragUndGruppe(persistenterMailversandAuftrag,
				persistenteMailversandGruppe,
				nextMailversandgruppe.getEmpfaengerEmails().size(), Jobstatus.ERRORS);

			anzahlVersendet = persistenterMailversandAuftrag.getAnzahlVersendet();
		} catch (Exception e) {

			LOGGER.error("Unerwartete Exception beim Mailversand an Grupppe {}: {}", persistenteMailversandGruppe.getUuid(),
				e.getMessage(), e);
		} finally {

			if (anzahlEmpfaenger == anzahlVersendet) {

				markCompleted(persistenterMailversandAuftrag);
			}
		}

	}

	PersistenterMailversandauftrag markStarded(final PersistenterMailversandauftrag mailversandauftrag) {

		mailversandauftrag.setVersandBegonnenAm(LocalDateTime.now());
		mailversandauftrag.setStatus(Jobstatus.IN_PROGRESS);
		mailversandauftrag.setGeaendertAm(new Date());

		return dao.updateMailversandauftrag(mailversandauftrag);

	}

	void markCompleted(final PersistenterMailversandauftrag versandauftrag) {

		Jobstatus status = versandauftrag.isVersandMitFehlern() ? Jobstatus.ERRORS : Jobstatus.COMPLETED;

		if (versandauftrag.getStatus() == Jobstatus.CANCELLED) {

			// cancelled soll auch cancelled bleiben
			status = versandauftrag.getStatus();
		}

		versandauftrag.setGeaendertAm(new Date());
		versandauftrag.setVersandBeendetAm(LocalDateTime.now());
		versandauftrag.setStatus(status);

		dao.updateMailversandauftrag(versandauftrag);

		LOGGER.info("Mailversandauftrag {} als als beendet markiert: status={}, anzahlEmpfaenger={}, anzahlVersendet={}",
			StringUtils.abbreviate(versandauftrag.getUuid(), 11), status, versandauftrag.getAnzahlEmpfaenger(),
			versandauftrag.getAnzahlVersendet());

	}

	private PersistenterMailversandauftrag updateMailversandauftragUndGruppe(final PersistenterMailversandauftrag mailversandauftrag, final PersistenteMailversandgruppe gruppe, final long anzahlEmpfaenger, final Jobstatus statusGruppe) {

		long anzahlVersendet = mailversandauftrag.getAnzahlVersendet() + anzahlEmpfaenger;

		mailversandauftrag.setAnzahlVersendet(anzahlVersendet);
		mailversandauftrag.setGeaendertAm(new Date());

		if (!mailversandauftrag.isVersandMitFehlern() && Jobstatus.ERRORS == statusGruppe) {

			mailversandauftrag.setVersandMitFehlern(true);
		}

		gruppe.setGeaendertAm(LocalDateTime.now());
		gruppe.setStatus(statusGruppe);

		PersistenterMailversandauftrag result = dao.updateMailversandauftrag(mailversandauftrag);
		dao.updateMailversandgruppe(gruppe);

		return result;
	}

	void sendeMails(final PersistenterInfomailTextReadOnly infomailText, final Mailversandgruppe gruppe) {

		AuthAdminMailDto mailDto = new AuthAdminMailDto().withAttachSpammailhinweis(true)
			.withBccEmpfaenger(gruppe.getEmpfaengerEmails()).withBetreff(infomailText.betreff).withBody(infomailText.mailtext);

		mailService.sendeMail(mailDto);

	}
}
