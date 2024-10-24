import { AuthSession, undefinedAuthSession } from 'apps/auth-app/src/app/auth/model';
import { createFeature, createReducer, on } from "@ngrx/store";
import { authActions } from './auth.actions';

export interface AuthState {
    session: AuthSession;
}

const initialAuthState: AuthState = {
    session: undefinedAuthSession
}


export const authFeature = createFeature({
    name: 'auth',
    reducer: createReducer(
        initialAuthState,
        on(authActions.aNONYMOUS_SESSION_CREATED, (state, action) => {
            return {
                ...state,
                session: action.authSession
            };
        }),
        on(authActions.cLEAR_SESSION, (state, action) => ({...initialAuthState}))
    )
})