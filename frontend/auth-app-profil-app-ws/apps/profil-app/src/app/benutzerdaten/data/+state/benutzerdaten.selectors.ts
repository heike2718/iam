import { createSelector } from '@ngrx/store';
import { benutzerdatenFeature } from './benutzerdaten.reducer';

const { selectBenutzerdatenState } = benutzerdatenFeature;


const benutzerdaten = createSelector(
    selectBenutzerdatenState,
    (state) => state.benutzerdaten
)

export const fromBenutzerdaten = {
    benutzerdaten
}