export interface AuthSession {
    sessionId: string;
    csrfToken: string;
    expiresAt: number; // expiration in milliseconds after 01.01.1970
}

export const undefinedAuthSession: AuthSession = { csrfToken: '', expiresAt: 0, sessionId: '' };

