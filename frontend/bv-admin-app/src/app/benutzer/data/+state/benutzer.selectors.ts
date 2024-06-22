
import { createSelector } from '@ngrx/store';
import { benutzerFeature } from './benutzer.reducer';

const {selectBenutzerState: selectBenutzerState} = benutzerFeature

const anzahlTreffer = createSelector(
    selectBenutzerState,
    (state) => state.anzahlTreffer
)

const page = createSelector(
    selectBenutzerState,
    (state) => state.page
)

const paginationState = createSelector(
    selectBenutzerState,
    (state) => state.paginationState
)

const benutzerTableFilter = createSelector(
    selectBenutzerState,
    (state) => state.benutzerTableFilter
)

export const fromBenutzer = {
    anzahlTreffer,
    page,
    paginationState,
    benutzerTableFilter: benutzerTableFilter
}
