import { createFeature, createReducer, on } from "@ngrx/store";
import { changeTempPasswordActions } from './change-temp-password.actions';


export interface ChangeTempPasswordState {
    readonly submittingForm: boolean;
    readonly tempPasswordSuccess: boolean;
    readonly tempPasswordSuccessMessage: string | undefined;
}

const initialState: ChangeTempPasswordState = {
    submittingForm: false,
    tempPasswordSuccess: false,
    tempPasswordSuccessMessage: undefined
};

export const changeTempPasswordFeature = createFeature({
    name: 'tempPassword',
    reducer: createReducer<ChangeTempPasswordState>(
        initialState,
        on(changeTempPasswordActions.cHANGE_TEMP_PASSWORD, (state, _action) => {
            return { ...state, submittingForm: true, tempPasswordSuccess: false, tempPasswordSuccessMessage: undefined };
        }),
        on(changeTempPasswordActions.tEMP_PASSWORD_CHANGED, (state, action) => {
            return { ...state, submittingForm: false, tempPasswordSuccess: true, tempPasswordSuccessMessage: action.payload.message.message };
        }),
        on(changeTempPasswordActions.rESET_CHANGE_TEMP_PASSWORD_STATE, (_state,_action) => initialState)
    )
});
