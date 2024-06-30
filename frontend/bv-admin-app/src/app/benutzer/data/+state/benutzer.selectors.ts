
import { createSelector } from '@ngrx/store';
import { benutzerFeature } from './benutzer.reducer';

const {selectBenutzerState: selectBenutzerState} = benutzerFeature

const anzahlTreffer = createSelector(
    selectBenutzerState,
    (state) => state.paginationState.anzahlTreffer
)

const paginationState = createSelector(
    selectBenutzerState,
    (state) => state.paginationState
)

const page = createSelector(
    selectBenutzerState,
    (state) => state.page
)

const benutzerBasket = createSelector(
    selectBenutzerState,
    (state) => state.benutzerBasket
)

const filterValues = createSelector(
    selectBenutzerState,
    (state) => state.filterValues
)

export const fromBenutzer = {
    anzahlTreffer,
    paginationState,
    page,
    benutzerBasket,
    filterValues
}
