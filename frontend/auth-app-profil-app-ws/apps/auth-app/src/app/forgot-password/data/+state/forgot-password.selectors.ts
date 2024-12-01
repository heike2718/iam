import { createSelector } from '@ngrx/store';
import { forgotPasswordFeature } from './forgot-password.reducer';

const { selectForgotPasswordState } = forgotPasswordFeature;

const submittingForm = createSelector(
    selectForgotPasswordState,
    (state) => state.submittingForm
)

const tempPasswordSuccess = createSelector(
    selectForgotPasswordState,
    (state) => state.tempPasswordSuccess
)

export const fromForgotPassword = {
    submittingForm,
    tempPasswordSuccess
}