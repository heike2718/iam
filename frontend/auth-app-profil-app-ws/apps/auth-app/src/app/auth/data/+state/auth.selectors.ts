import { createSelector } from "@ngrx/store";
import { authFeature } from './auth.reducer';

const { selectAuthState: selectAuthSatate } = authFeature;

const authSession = createSelector(
    selectAuthSatate,
    (state) => state.session
)

export const fromAuth = {
    authSession
}