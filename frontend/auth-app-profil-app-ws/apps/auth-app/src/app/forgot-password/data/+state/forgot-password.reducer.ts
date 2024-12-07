import { createFeature, createReducer, on } from "@ngrx/store";
import { forgotPasswordActions } from './forgot-password.actions';


export interface ForgotPasswordState {
    readonly submittingForm: boolean;
    readonly tempPasswordSuccess: boolean;
    readonly tempPasswordSuccessMessage: string | undefined;
}

const initialState: ForgotPasswordState = {
    submittingForm: false,
    tempPasswordSuccess: false,
    tempPasswordSuccessMessage: undefined
};

export const forgotPasswordFeature = createFeature({
    name: 'forgotPassword',
    reducer: createReducer<ForgotPasswordState>(
        initialState,
        on(forgotPasswordActions.oRDER_TEMP_PASSWORD, (state, _action) => {
            return { ...state, submittingForm: true, tempPasswordSuccess: false, tempPasswordSuccessMessage: undefined };
        }),
        on(forgotPasswordActions.tEMP_PASSWORD_SUCCESS, (state, action) => {
            return { ...state, submittingForm: false, tempPasswordSuccess: true, tempPasswordSuccessMessage: action.payload.message };
        }),
        on(forgotPasswordActions.rESET_ORDER_TEMP_PASSWORD_STATE, (_state,_action) => initialState)
    )
});
