import { Benutzer, BenutzersucheFilterAndSortValues, addBenutzerIfNotContained, initialBenutzersucheFilterAndSortValues, removeBenutzer, removeBenutzers } from "@bv-admin-app/benutzer/model";
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
        on(benutzerActions.bENUTZER_PAGEDEFINITION_CHANGED, (state, action) => {
            return {
                ...state,
                paginationState: {...state.paginationState, pageDefinition: action.pageDefinition}
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
        on(benutzerActions.rEMOVE_SINGLE_BENUTZER_FROM_BASKET, (state, action) => {
            
            const actuallyDeselected: Benutzer[] = [];
            actuallyDeselected.push(action.benutzer);
            const newBasket = removeBenutzers([...state.benutzerBasket], actuallyDeselected);
            
            return {
                ...state,
                benutzerBasket: newBasket
            }
        }),
        on(benutzerActions.sINGLE_BENUTZER_DELETED, (state, action) => {
            
            const newBasket = removeBenutzer([...state.benutzerBasket], action.responsePayload.uuid);
            const newPage = removeBenutzer([...state.page], action.responsePayload.uuid);
            
            return {
                ...state,
                page: newPage,
                benutzerBasket: newBasket
            }
        }),
        on(benutzerActions.rESET_BENUTZERBASKET, (state, action) => {
            swallowEmptyArgument(action, false);
            return {...state, benutzerBasket: []};
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

