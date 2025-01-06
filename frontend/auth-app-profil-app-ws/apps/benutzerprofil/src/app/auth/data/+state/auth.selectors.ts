import { createSelector } from '@ngrx/store';
import { authFeature } from './auth.reducer';

const { selectBenutzerdatenAuthState } = authFeature;

const session = createSelector(
    selectBenutzerdatenAuthState,
    (state) => state.session
)

export const fromAuth = {
    session
}
