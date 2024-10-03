export interface AuthResult {
	expiresAt?: number;
	state?: string;
	nonce?: string;
	idToken?: string;
}

export interface UserSession {
	sessionId: string;
	csrfToken: string;
	fullName: string;
	expiresAt: number; // expiration in milliseconds after 01.01.1970
	idReference: string;
}

export const anonymousSession: UserSession = {
	sessionId: '',
	csrfToken: '',
	fullName: 'Gast',
	expiresAt: 0,
	idReference: ''
}