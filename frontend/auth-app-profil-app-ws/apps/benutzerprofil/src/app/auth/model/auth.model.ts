export const AUTH_FEATURE_KEY = 'benutzerdatenAuth';

export interface AuthResult {
    expiresAt: number | undefined;
    state: string | undefined;
    nonce: string | undefined;
    idToken: string | undefined;
}

export interface Session {
    readonly sessionId: string | undefined;
    readonly expiresAt: number;
}

export const anonymousSession: Session = {
    sessionId: undefined,
    expiresAt: 0
}
