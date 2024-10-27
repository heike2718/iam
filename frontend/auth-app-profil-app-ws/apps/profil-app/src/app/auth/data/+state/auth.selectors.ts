import { createSelector } from '@ngrx/store';
import { authFeature } from './auth.reducer';

const { selectProfilAuthState } = authFeature;

const session = createSelector(
    selectProfilAuthState,
    (state) => state.session
)

export const fromAuth = {
    session
}
