
import { createFeature, createReducer, on } from '@ngrx/store';
import { authActions } from './auth.actions';
import { swallowEmptyArgument } from '@bv-admin/shared/util';
import { AUTH_FEATURE_KEY, Session, anonymousSession } from '@bv-admin/shared/auth/model';

export interface AuthState {
    readonly session: Session;
    readonly sessionExists: boolean;
}

export const initialState: AuthState = {
    session: anonymousSession,
    sessionExists: false
}

export const authFeature = createFeature({
    name: AUTH_FEATURE_KEY,
    reducer: createReducer<AuthState>(
        initialState,
        on(
            authActions.sESSION_CREATED,
            (state, { session: session }): AuthState => {
                return {
                    ...state,
                    session: session
                };
            }
        ),
        on(authActions.lOGGED_OUT, (state, action) => {
            swallowEmptyArgument(action, false);
            return {
                ...state,
                session: anonymousSession
            }
        })
    )
})
