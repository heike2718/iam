import { SignUpCredentials } from "@authprovider/login-signup/model";
import { ClientCredentials, ClientInformation, LoginCredentials, SignUpLogInResponseData } from "@authprovider/model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";

export const loginSignupActions = createActionGroup({
    source: 'LoginSignup',
    events: {
        'LOAD_CLIENT_INFORMATION': props<{clientCredentials: ClientCredentials}>(),
        'CLIENT_INFORMATION_LOADED': props<{clientInformation: ClientInformation}>(),
        'SIGN_UP': props<{ signUpCredentials: SignUpCredentials }>(),
        'LOG_IN': props<{loginCredentials: LoginCredentials}>(),
        'LOG_IN_SIGN_UP_SUCCESS': props<{ signUpLogInResponseData: SignUpLogInResponseData }>(),
        'RESET_LOGIN_SIGNUP_STATE': emptyProps()
    }
});
