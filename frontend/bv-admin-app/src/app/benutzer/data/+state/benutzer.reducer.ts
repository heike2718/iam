import { Benutzer, BenutzersucheGUIModel, initialBenutzersucheGUIModel } from "@bv-admin-app/benutzer/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { benutzerActions } from './benutzer.actions';
import { loggedOutAction } from '@bv-admin-app/shared/auth/data';
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";

export interface BenutzerState {
    readonly page: Benutzer[];
    readonly guiModel: BenutzersucheGUIModel;
}

const initialBenutzerState: BenutzerState = {
    page: [],
    guiModel: initialBenutzersucheGUIModel
}

export const benutzerFeature = createFeature({
    name: 'benutzer',
    reducer: createReducer(
        initialBenutzerState,
        on(benutzerActions.bENUTZER_FOUND, (state, action) => {
            return {
                ...state,
                guiModel: {...state.guiModel, anzahlTreffer: action.treffer.anzahlGesamt},
                page: action.treffer.items
            };
        }),
        on(benutzerActions.gUI_MODEL_CHANGED, (state, action) => {
            return {
                ...state,
                guiModel: action.guiModel
            };
        }),
        
        on(benutzerActions.sUCHE_ZURUECKSETZEN, (_state, action) => {
            swallowEmptyArgument(action, false);
            return initialBenutzerState;
        }),

        on(loggedOutAction, (_state, action) => {
            swallowEmptyArgument(action, false);
            return initialBenutzerState;
        })
    )
})