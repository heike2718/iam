import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { LoginSignupEffects } from '../data/+state/login-signup.effects';
import { loginSignUpFeature } from '../data/+state/login-signup.reducer';


export const loginSignupDataProvider = [
    provideState(loginSignUpFeature),
    provideEffects(LoginSignupEffects)
]