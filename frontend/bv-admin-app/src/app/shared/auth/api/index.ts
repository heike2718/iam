import { authActions } from "@bv-admin-app/shared/auth/data";

export * from './auth.facade';
export * from './auth-data.provider';

// nur diese Action soll exportiert werden, damit andere module reagiren können. Alle anderen bleiben privat hier in auth/data.
export const loggedOutEvent = authActions.lOGGED_OUT; 