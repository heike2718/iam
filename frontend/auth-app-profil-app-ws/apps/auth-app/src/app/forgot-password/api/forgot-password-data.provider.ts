import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { ForgotPasswordEffects, forgotPasswordFeature } from "@auth-app/forgot-password/data";


export const forgotPasswordDataProvider = [
    provideState(forgotPasswordFeature),
    provideEffects(ForgotPasswordEffects)
]