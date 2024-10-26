export interface AuthResult {
	expiresAt?: number;
	state?: string;
	nonce?: string;
	idToken?: string;
}

export interface User {
	readonly loginName: string;
	readonly email: string;
	readonly vorname: string;
	readonly nachname: string;
	readonly fullName: string;
}

export const anonymousUser: User = {
	loginName: '',
	email: '',
	vorname: '',
	nachname: '',
	fullName: 'Gast'
}

export interface Session {
	readonly sessionId: string;
	readonly csrfToken: string;
	readonly fullName: string;
	readonly expiresAt: number; // expiration in milliseconds after 01.01.1970
	readonly idReference: string;
	readonly user: User;
}

export const anonymousSession: Session = {
	sessionId: '',
	csrfToken: '',
	fullName: 'Gast',
	expiresAt: 0,
	idReference: '',
	user: anonymousUser
}

