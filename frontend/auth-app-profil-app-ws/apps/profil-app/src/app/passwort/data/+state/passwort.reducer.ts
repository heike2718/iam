import { createFeature, createReducer, on } from "@ngrx/store";
import { passwortActions } from "./passwort.actions";
import { PasswortPayload, initialPasswortPayload } from "@ap-ws/common-model";


export interface PasswortState {
    readonly passwortPayload: PasswortPayload ;
}

const initialState: PasswortState = {
    passwortPayload: initialPasswortPayload
}

export const passwortFeature = createFeature({
    name: 'passwort',
    reducer: createReducer<PasswortState>(
        initialState,
        on(passwortActions.lOCALLY_STORE_PASSWORT, (state, action) => {
            return {...state, passwortPayload: action.passwortPayload};
        }),
        on(passwortActions.pASSWORT_GEAENDERT, (_state, _action) => {
            return initialState;
        }),
        on(passwortActions.rESET_PASSWORT, (_state, _action) => {
            return initialState;
        })
    )
})

