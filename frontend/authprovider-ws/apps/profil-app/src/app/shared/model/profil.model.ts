import { isValidPassword, TwoPasswords, isTwoPasswordsValid } from "@authprovider-ws/common-components";

export const STORAGE_KEY_ID_REFERENCE = 'prf_id_reference';
export const STORAGE_KEY_FULL_NAME = 'prf_full_name';
export const STORAGE_KEY_SESSION_EXPIRES_AT = 'prf_session_expires_at';
export const STORAGE_KEY_DEV_SESSION_ID = 'prf_dev_session_id';

export type AccountAction = 'change data' | 'change password' | 'delete account';


export interface AuthResult {
	expiresAt?: number;
	state?: string;
	nonce?: string;
	idToken?: string;
}

export interface UserSession {
	sessionId?: string;
	csrfToken: string;
	fullName?: string;
	expiresAt: number; // expiration in milliseconds after 01.01.1970
	idReference: string;
}

export interface OAuthAccessTokenPayload {
	accessToken: string;
	expiresAt: number;
}


export interface ChangePasswordPayload {
	passwort: string;
	twoPasswords: TwoPasswords;
}

export interface ProfileDataPayload {

	loginName: string;
	email: string;
	vorname?: string;
	nachname?: string;

}

export interface User {
	loginName?: string;
	email: string;
	vorname?: string;
	nachname?: string;
}

export interface RefreshAccessTokenPayload {
	clientAccessToken: string[];
	userRefreshToken: string;
	force: boolean;
}

export interface AuthenticatedUser {
	session: UserSession;
	user: User;
}

export function isKnownUser(user: User): boolean {
	return user && user.email !== undefined;
}

export function isChangePasswordPayloadValid(payload: ChangePasswordPayload): boolean {

	if (!payload || !payload.passwort || !payload.twoPasswords ) {
		return true;
	}

	if (!isValidPassword(payload.passwort)) {
		return false;
	}

	if (!isTwoPasswordsValid(payload.twoPasswords)) {
		return false;
	}
	
	return true;
}

