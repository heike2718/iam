import { authActions } from "./+state/auth.actions";

// nur diese Actions sollen exportiert werden, alle anderen bleiben privat hier in auth/data.
export const logOutAction = authActions.lOG_OUT;
export const loggedOutAction = authActions.lOGGED_OUT; 
export const initSessionAction = authActions.iNIT_SESSION;
export const requestLoginUrlAction = authActions.rEQUEST_LOGIN_URL;