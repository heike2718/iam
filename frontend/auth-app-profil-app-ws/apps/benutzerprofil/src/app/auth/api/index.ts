import { authActions } from '@benutzerprofil/auth/data';

export * from './auth.facade';
export { authDataProvider } from './auth-data.provider';

// nur diese Action soll exportiert werden, damit andere module reagiren können. Alle anderen bleiben privat hier in auth/data.
export const loggedOutEvent = authActions.lOGGED_OUT;