import { Benutzer, BenutzerTableFilter, PaginationState, initialBenutzerTableFilter, initialPaginationState } from "@bv-admin-app/benutzer/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { benutzerActions } from './benutzer.actions';
import { loggedOutAction } from '@bv-admin-app/shared/auth/data';
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";

export interface BenutzerState {
    readonly anzahlTreffer: number;
    readonly page: Benutzer[];
    readonly benutzerTableFilter: BenutzerTableFilter;
    readonly paginationState: PaginationState;
}

const initialBenutzerState: BenutzerState = {
    anzahlTreffer: 0,
    page: [],
    benutzerTableFilter: initialBenutzerTableFilter,
    paginationState: initialPaginationState
}

export const benutzerFeature = createFeature({
    name: 'benutzer',
    reducer: createReducer(
        initialBenutzerState,
        on(benutzerActions.bENUTZER_FOUND, (state, action) => {
            return {
                ...state,
                anzahlTreffer: action.treffer.anzahlGesamt,
                page: action.treffer.items};
        }),

        on(benutzerActions.bENUTZER_SELECT_PAGE, (state, _action) => {
            return {...state};
        }),

        on(benutzerActions.bENUTZER_TABLE_FILTER_CHANGED, (state, _action) => {
            return {...state};
        }),

        on(loggedOutAction, (_state, action) => {
            swallowEmptyArgument(action, false);
            return initialBenutzerState;
        })
    )
})