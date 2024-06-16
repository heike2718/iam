import { createSelector } from '@ngrx/store';
import { authFeature } from './auth.reducer';

const { selectBvAuthState } = authFeature;

const session = createSelector(
    selectBvAuthState,
    (state) => state.session
)

const user = createSelector(
    selectBvAuthState,
    (state) => state.session.user
)

export const fromAuth = {
    session,
    user
}
