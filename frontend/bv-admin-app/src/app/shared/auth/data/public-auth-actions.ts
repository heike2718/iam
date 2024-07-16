import { authActions } from "./+state/auth.actions";

// nur diese Action soll exportiert werden, damit andere module reagiren können. Alle anderen bleiben privat hier in auth/data.
export const loggedOutEvent = authActions.lOGGED_OUT; 