import { createFeature, createReducer, on } from "@ngrx/store";
import { loginSignupActions  } from "./login-signup.actions";
import { ClientCredentials, ClientInformation, createHashForRedirectUrl } from "@authprovider/model";


export interface SignUpState {
    readonly clientCredentials: ClientCredentials | undefined;
    readonly clientInformation: ClientInformation | undefined;
    readonly submittingForm: boolean;
    readonly redirectUrl: string | undefined;
}

const initialState: SignUpState = {
    clientCredentials: undefined,
    clientInformation: undefined,
    submittingForm: false,
    redirectUrl: undefined
};

export const loginSignUpFeature = createFeature({
    name: 'loginSignup',
    reducer: createReducer<SignUpState>(
        initialState,
        on(loginSignupActions.lOAD_CLIENT_INFORMATION, (state,action) => {
            return {...state, clientCredentials: action.clientCredentials}
        }),
        on(loginSignupActions.cLIENT_INFORMATION_LOADED, (state,action) => {
            return {...state, clientInformation: action.clientInformation}
        }),
        on(loginSignupActions.sIGN_UP, (state, _action) => {
            return { ...state, submittingForm: true };
        }),
        on(loginSignupActions.lOG_IN, (state, _action) => {
            return { ...state, submittingForm: true };
        }),
        on(loginSignupActions.lOG_IN_SIGN_UP_SUCCESS, (state, action) => {

            const hash = createHashForRedirectUrl(action.signUpLogInResponseData);
            return { ...state, submittingForm: false, clientCredentials: undefined, redirectUrl: state.clientCredentials?.redirectUrl + hash };
        }),
        on(loginSignupActions.rESET_LOGIN_SIGNUP_STATE, (_state,_action) => initialState)
    )
});
