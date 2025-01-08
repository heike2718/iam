import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { LoginSignupEffects, loginSignUpFeature } from "@authprovider/login-signup/data";


export const loginSignupDataProvider = [
    provideState(loginSignUpFeature),
    provideEffects(LoginSignupEffects)
]