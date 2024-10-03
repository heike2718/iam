import { createFeature, createReducer, on } from "@ngrx/store";
import { anonymousSession, UserSession } from "@profil-app/auth/model";
import { authActions } from "./auth.actions";


export interface AuthState {
    readonly session: UserSession;
};

const initialState: AuthState = {
    session: anonymousSession
}

export const authFeature = createFeature({
    name: 'authFeature',
    reducer: createReducer<AuthState>(
        initialState,
        on(
            authActions.sESSION_CREATED,
            (state, { session: session }): AuthState => {
                return { ...state, session }
            }),
            on(authActions.lOGGED_OUT, (_state, _action) => initialState)
    )
})