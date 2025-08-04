import { createSelector } from '@ngrx/store';
import { benutzerdatenFeature } from './benutzerdaten.reducer';
import { isAnonymerBenutzer } from '@benutzerprofil/benutzerdaten/model';

const { selectBenutzerdatenState } = benutzerdatenFeature;


const benutzerdaten = createSelector(
    selectBenutzerdatenState,
    (state) => state.benutzerdaten
)

const isBenutzerLoaded = createSelector(
    selectBenutzerdatenState,
    (state) => isAnonymerBenutzer(state.benutzerdaten) === false
)

const isExistierenderBenutzer = createSelector(
    isBenutzerLoaded,
    (loaded) => !loaded
)

export const fromBenutzerdaten = {
    benutzerdaten,
    isExistierenderBenutzer,
    isBenutzerLoaded
}