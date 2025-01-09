
import { createSelector } from '@ngrx/store';
import { benutzerFeature } from './benutzer.reducer';
import { SortDirection } from '@angular/material/sort';
import { SortDefinition } from '@bv-admin/shared/model';

const {selectBenutzerState: benutzerState} = benutzerFeature

const anzahlTreffer = createSelector(
    benutzerState,
    (state) => state.paginationState.anzahlTreffer
)

const paginationState = createSelector(
    benutzerState,
    (state) => state.paginationState
)

const page = createSelector(
    benutzerState,
    (state) => state.page
)

const benutzerBasket = createSelector(
    benutzerState,
    (state) => state.benutzerBasket
)

const lengthBenutzerBasket = createSelector(
    benutzerBasket,
    (basket) => basket.length
)

const uuidsGewaehlteBenutzer = createSelector (
    benutzerBasket,
    (basket) => basket.map(b => b.uuid)
)

const filterValues = createSelector(
    benutzerState,
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
    uuidsGewaehlteBenutzer,
    filterValues,
    sortDefinition
}
