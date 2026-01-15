import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { ForgotPasswordEffects } from '../data/+state/forgot-password.effects';
import { forgotPasswordFeature } from '../data/+state/forgot-password.reducer';


export const forgotPasswordDataProvider = [
    provideState(forgotPasswordFeature),
    provideEffects(ForgotPasswordEffects)
]