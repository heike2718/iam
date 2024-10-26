import { createSelector } from "@ngrx/store";
import { authFeature } from './auth.reducer';
import { anonymousSession } from "apps/profil-app/src/app/auth/model";

const { selectAuthFeatureState, selectSession } = authFeature;

const session = createSelector(
    selectAuthFeatureState,
    (state) => state.session
);

const isAnonymous = createSelector(
    selectSession,
    (session) => anonymousSession.fullName === session.fullName
)

const isLoggedIn = createSelector(
    isAnonymous,
    (anonymous) => !anonymous
)

const isLoggedOut = createSelector(
    isAnonymous,
    (anonymous) => anonymous
)

const user = createSelector(
    selectAuthFeatureState,
    (state) => state.session.user
)

export const fromAuth = {
    session,
    user,
    isLoggedIn,
    isLoggedOut
}