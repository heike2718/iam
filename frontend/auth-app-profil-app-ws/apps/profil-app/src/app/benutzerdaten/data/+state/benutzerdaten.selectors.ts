import { createSelector } from '@ngrx/store';
import { benutzerdatenFeature } from './benutzerdaten.reducer';
import { isAnonymerBenutzer } from '@profil-app/benutzerdaten/model';

const { selectBenutzerdatenState } = benutzerdatenFeature;


const benutzerdaten = createSelector(
    selectBenutzerdatenState,
    (state) => state.benutzerdaten
)

const isExistierenderBenutzer = createSelector(
    benutzerdaten,
    (benutzerdaten) => !isAnonymerBenutzer(benutzerdaten)
)

export const fromBenutzerdaten = {
    benutzerdaten,
    isExistierenderBenutzer
}