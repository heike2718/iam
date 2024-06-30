import { Benutzer, BenutzersucheFilterAndSortValues, addBenutzerIfNotContained, initialBenutzersucheFilterAndSortValues, removeBenutzers } from "@bv-admin-app/benutzer/model";
import { PaginationState, initialPaginationState } from '@bv-admin-app/shared/model'
import { createFeature, createReducer, on } from "@ngrx/store";
import { benutzerActions } from './benutzer.actions';
import { loggedOutAction } from '@bv-admin-app/shared/auth/data';
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";

export interface BenutzerState {
    readonly page: Benutzer[];
    readonly paginationState: PaginationState;
    readonly filterValues: BenutzersucheFilterAndSortValues;
    readonly benutzerBasket: Benutzer[];
    readonly selectedBenutzer: Benutzer | undefined;
}

const initialBenutzerState: BenutzerState = {
    page: [],
    paginationState: initialPaginationState,
    filterValues: initialBenutzersucheFilterAndSortValues,
    benutzerBasket: [],
    selectedBenutzer: undefined
}

export const benutzerFeature = createFeature({
    name: 'benutzer',
    reducer: createReducer(
        initialBenutzerState,
        on(benutzerActions.bENUTZER_FOUND, (state, action) => {
            return {
                ...state,
                paginationState: { ...state.paginationState, anzahlTreffer: action.treffer.anzahlGesamt },
                page: action.treffer.items
            };
        }),
        on(benutzerActions.bENUTZER_FILTER_CHANGED, (state, action) => {
            return {
                ...state,
                filterValues: action.filter
            }
        }),
        on(benutzerActions.sELECTIONSUBSET_CHANGED, (state, action) => {

            const actuallySelected: Benutzer[] = action.actuallySelected;
            const actuallyDeselected: Benutzer[] = action.actuallyDeselected;
            const actualBasket = removeBenutzers([...state.benutzerBasket], actuallyDeselected);
            const newBasket = addBenutzerIfNotContained([...actualBasket], actuallySelected);

            return {
                ...state,
                benutzerBasket: newBasket
            }
        }),
        on(benutzerActions.bENUTZERBASKET_CHANGED, (state, action) => {

            const actualSelectionSubset: Benutzer[] = action.selection;
            const actualBasket = [...state.benutzerBasket];

            const newBasket: Benutzer[] = [];

            actualBasket.forEach(b => {
                const ben: Benutzer[] = actualSelectionSubset.filter(s => s.uuid === b.uuid);
            })

            return {
                ...state,
                benutzerBasket: newBasket
            }
        }),
        on(benutzerActions.rESET_FILTER, (state, action) => {
            swallowEmptyArgument(action, false);
            return {...initialBenutzerState, benutzerBasket: [...state.benutzerBasket]};
        }),

        on(loggedOutAction, (_state, action) => {
            swallowEmptyArgument(action, false);
            return initialBenutzerState;
        })
    )
})