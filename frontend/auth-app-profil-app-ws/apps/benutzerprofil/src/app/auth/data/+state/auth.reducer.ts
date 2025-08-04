import { createFeature, createReducer, on } from "@ngrx/store";
import { anonymousSession, Session, AUTH_FEATURE_KEY } from "@benutzerprofil/auth/model";
import { authActions } from "./auth.actions";

export interface AuthState {
  readonly session: Session;
};

const initialState: AuthState = {
  session: anonymousSession
}

export const authFeature = createFeature({
  name: AUTH_FEATURE_KEY,
  reducer: createReducer<AuthState>(
    initialState,
    on(
      authActions.sESSION_CREATED,
      (state, { session: session }): AuthState => {
        return { ...state, session }
      }),
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    on(authActions.lOGGED_OUT, (_state, _action) => initialState)
  )
})
