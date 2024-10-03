import { createSelector } from "@ngrx/store";
import { authFeature } from './auth.reducer';
import { anonymousSession } from "@profil-app/auth/model";

const { selectAuthFeatureState, selectSession } = authFeature;

const session = createSelector(
    selectAuthFeatureState,
    (state) => state.session
);

const userName = createSelector(
    selectSession,
    (session) => session.fullName
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

export const fromAuth = {
    session,
    userName,
    isLoggedIn,
    isLoggedOut
}