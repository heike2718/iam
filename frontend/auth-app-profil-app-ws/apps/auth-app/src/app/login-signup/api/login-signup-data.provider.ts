import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { LoginSignupEffects, loginSignUpFeature } from "@auth-app/login-signup/data";


export const loginSignupDataProvider = [
    provideState(loginSignUpFeature),
    provideEffects(LoginSignupEffects)
]