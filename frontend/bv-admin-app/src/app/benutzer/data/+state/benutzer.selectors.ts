
import { createSelector } from '@ngrx/store';
import { benutzerFeature } from './benutzer.reducer';
import { SortDirection } from '@angular/material/sort';
import { SortDefinition } from '@bv-admin-app/shared/model';

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

const lengthBenutzerBasket = createSelector(
    benutzerBasket,
    (basket) => basket.length
)

const filterValues = createSelector(
    selectBenutzerState,
    (state) => state.filterValues
)

const sortLabelName = createSelector(
    filterValues,
    (filter) => filter.sortByLabelname === null ? '' : filter.sortByLabelname
)

const sortDirection = createSelector(
    paginationState,
    (paginationState) => paginationState.pageDefinition.sortDirection as SortDirection
)

const sortDefinition = createSelector(
    sortDirection,
    sortLabelName,
    (direction, active): SortDefinition => ({direction: direction, active: active}) 
)

export const fromBenutzer = {
    anzahlTreffer,
    paginationState,
    page,
    benutzerBasket,
    lengthBenutzerBasket,
    filterValues,
    sortDefinition
}
