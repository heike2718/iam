import { Benutzer, BenutzersucheFilterValues, initialBenutzersucheFilterValues } from "@bv-admin-app/benutzer/model";
import { PaginationState, initialPaginationState} from '@bv-admin-app/shared/model'
import { createFeature, createReducer, on } from "@ngrx/store";
import { benutzerActions } from './benutzer.actions';
import { loggedOutAction } from '@bv-admin-app/shared/auth/data';
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";

export interface BenutzerState {
    readonly page: Benutzer[];
    readonly paginationState: PaginationState;
    readonly filterValues: BenutzersucheFilterValues;
    readonly tableBenutzerSelection: Benutzer[];
    readonly selectedBenutzer: Benutzer | undefined;
}

const initialBenutzerState: BenutzerState = {
    page: [],
    paginationState: initialPaginationState,
    filterValues: initialBenutzersucheFilterValues,
    tableBenutzerSelection: [],
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
        on(benutzerActions.tABLE_BENUTZERSELECTION_CHANGED, (state, action) => {
            return {
                ...state,
                tableBenutzerSelection: action.selection
            }
        }),
        on(benutzerActions.rESET_FILTER, (_state, action) => {
            swallowEmptyArgument(action, false);
            return initialBenutzerState;
        }),

        on(loggedOutAction, (_state, action) => {
            swallowEmptyArgument(action, false);
            return initialBenutzerState;
        })
    )
})