import { isEmpty, isTwoPasswordsValid, isValidEmail, isValidPassword } from "@authprovider-ws/common-components";

export const STORAGE_KEY_SESSION_EXPIRES_AT = 'auth_session_expires_at';
export const STORAGE_KEY_DEV_SESSION_ID = 'auth_dev_session_id';
export const HEADER_NAME_SESSION_ID = 'X-SESSIONID';

export interface AuthSession {
	sessionId?: string;
	csrfToken: string;
	expiresAt: number; // expiration in milliseconds after 01.01.1970
}

export interface ClientCredentials {
	accessToken: string;
	redirectUrl: string;
	state: string;
}

export interface ClientInformation {
	// clientId: string;
	name: string;
	zurueckText: string;
	agbUrl: string;
	loginnameSupported: boolean;
	namenRequired: boolean;
	baseUrl: string;
}

export interface AuthorizationCredentials {
	loginName: string;
	passwort: string;
	kleber: string;
}

export interface LoginCredentials {
	authorizationCredentials: AuthorizationCredentials;
	clientCredentials: ClientCredentials;
	nonce: string;
}

export interface TwoPasswords {
	passwort: string;
	passwortWdh: string;
}

export interface RegistrationCredentials {
	email: string;
	loginName: string;
	vorname?: string;
	nachname?: string;
	groups?: string;
	nonce?: string;
	agbGelesen: boolean;
	twoPasswords: TwoPasswords;
	kleber: string;
	clientCredentials: ClientCredentials;
}

export interface TempPasswordCredentials {
	email: string;
	clientCredentials: ClientCredentials;
	kleber: string;
}

export interface ChangeTempPasswordPayload {
	tokenId: string;
	tempPassword: string;
	email: string;
	twoPasswords: TwoPasswords;
}

export function createQueryParameters(clientCredentials: ClientCredentials) {
	return '?accessToken=' + clientCredentials.accessToken + '&redirectUrl=' + clientCredentials.redirectUrl + '&state=' + '';
}

export function isLoginCredentialsValid(loginCredentials: LoginCredentials): boolean {

	if (!loginCredentials || !loginCredentials.authorizationCredentials) {
		return false;
	}

	const authCredentials: AuthorizationCredentials = loginCredentials.authorizationCredentials;

	if (isEmpty(authCredentials.loginName) || isEmpty(authCredentials.passwort)) {
		return false;
	}

	if (authCredentials.loginName.length > 255) {
		return false;
	}

	if (!isValidPassword(authCredentials.passwort)) {
		return false;
	}

	return true;
}

export function isRegistrationPayloadValid(payload: RegistrationCredentials | undefined): boolean {

	if (!payload) {
		return false;
	}

	if (!isValidEmail(payload.email)) {
		return false;
	}

	if (payload.agbGelesen === false) {
		return false;
	}

	if (!isTwoPasswordsValid(payload.twoPasswords)) {
		return false;
	}

	if (!payload.vorname || payload.vorname === '') {
		return false;
	}

	if (!payload.nachname || payload.nachname === '') {
		return false;
	}

	if (!payload.loginName || payload.loginName === '') {
		return false;
	}

	return true;

}






