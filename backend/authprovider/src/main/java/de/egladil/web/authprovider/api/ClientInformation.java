//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.api;

import de.egladil.web.authprovider.entities.Client;

/**
 * ClientInformation sind die Attribute eines (AuthProvider-)Clients.
 */
public class ClientInformation {

	private String name;

	private String zurueckText;

	private String agbUrl;

	private boolean loginnameSupported;

	private boolean namenRequired;

	private String baseUrl;

	private String state;

	public static ClientInformation fromClient(final Client client) {

		ClientInformation data = new ClientInformation();
		data.name = client.getName();
		data.zurueckText = client.getZurueckText();
		data.agbUrl = client.getAgbUrl();
		data.loginnameSupported = client.isLoginWithLoginnameSupported();
		data.namenRequired = client.isVornameNachnameRequired();
		data.baseUrl = client.getBaseUrl();

		return data;
	}

	public String getAgbUrl() {
		return agbUrl;
	}

	public boolean isLoginnameSupported() {
		return loginnameSupported;
	}

	public String getName() {
		return name;
	}

	public String getZurueckText() {
		return zurueckText;
	}

	public boolean isNamenRequired() {
		return namenRequired;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(final String state) {
		this.state = state;
	}

}
