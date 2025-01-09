
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
    readonly sessionId: string | undefined;
    readonly expiresAt: number;
    readonly user: User;
}


const anonymousUser: User = {
    fullName: 'Gast',
    idReference: 'ANONYM',
    anonym: true
}

export const anonymousSession: Session = {
    sessionId: undefined,
    expiresAt: 0,
    user: anonymousUser
}
