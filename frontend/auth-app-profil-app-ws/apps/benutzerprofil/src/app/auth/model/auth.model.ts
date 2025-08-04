export const AUTH_FEATURE_KEY = 'benutzerdatenAuth';

export interface AuthResult {
    expiresAt: number | undefined;
    state: string | undefined;
    nonce: string | undefined;
    idToken: string | undefined;
}

export interface Session {
    readonly sessionActive: boolean; // true = Session existiert, false = ausgeloggt/abgelaufen
    readonly expiresAt: number;
}

export const anonymousSession: Session = {
    sessionActive: false,
    expiresAt: 0
}
