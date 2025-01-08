import { createSelector } from '@ngrx/store';
import { loginSignUpFeature } from './login-signup.reducer';

const { selectLoginSignupState } = loginSignUpFeature

const clientCredentials = createSelector(
    selectLoginSignupState,
    (state) => state.clientCredentials
)

const clientInformation = createSelector(
    selectLoginSignupState,
    (state) => state.clientInformation
)

const redirectUrl = createSelector(
    selectLoginSignupState,
    (state) => state.redirectUrl
)


export const fromLoginSignup = {
    clientCredentials,
    clientInformation,
    redirectUrl
}

