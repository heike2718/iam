import { createSelector } from '@ngrx/store';
import { changeTempPasswordFeature } from './change-temp-password.reducer';

const { selectTempPasswordState } = changeTempPasswordFeature;

const submittingForm = createSelector(
    selectTempPasswordState,
    (state) => state.submittingForm
)

const tempPasswordSuccess = createSelector(
    selectTempPasswordState,
    (state) => state.tempPasswordSuccess
)

const tempPasswordSuccessMessage = createSelector(
    selectTempPasswordState,
    (state) => state.tempPasswordSuccessMessage
)

export const fromChangeTempPassword = {
    submittingForm,
    tempPasswordSuccess,
    tempPasswordSuccessMessage
}