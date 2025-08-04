
export const AUTH_FEATURE_KEY = 'bvAuth';

export interface AuthResult {
    expiresAt: number | undefined;
    state: string | undefined;
    nonce: string | undefined;
    idToken: string | undefined;
}

export interface User {
    readonly idReference: string;
    readonly fullName: string;
    readonly anonym: boolean;
}

export interface Session {
    readonly sessionActive: boolean; // true = Session existiert, false = ausgeloggt/abgelaufen
    readonly expiresAt: number;
    readonly user: User;
}


const anonymousUser: User = {
    fullName: 'Gast',
    idReference: 'ANONYM',
    anonym: true
}

export const anonymousSession: Session = {
    sessionActive: false,
    expiresAt: 0,
    user: anonymousUser
}
