import { createFeature, createReducer, on } from "@ngrx/store";
import { anonymeBenutzerdaten, Benutzerdaten } from "@profil-app/benutzerdaten/model";
import { benutzerdatenActions } from "./benutzerdaten.actions";


export interface BenutzerdatenState {
    readonly benutzerdaten: Benutzerdaten;
}

const initialState: BenutzerdatenState = {
    benutzerdaten: anonymeBenutzerdaten
}

export const benutzerdatenFeature = createFeature({
    name: 'benutzerdaten',
    reducer: createReducer<BenutzerdatenState>(
        initialState,
        on(benutzerdatenActions.bENUTZERDATEN_LOADED, (state, action) => {
            return { ...state, benutzerdaten: action.benutzerdaten };
        }),
        on(benutzerdatenActions.bENUTZERDATEN_GEAENDERT, (state, action) => {
            return { ...state, benutzerdaten: action.responseDto.benutzer };
        }),
        on(benutzerdatenActions.rESET_BENUTZERDATEN, (_state, _action) => {
            return initialState;
        })
    )
})