import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { ForgotPasswordEffects, forgotPasswordFeature } from "@authprovider/forgot-password/data";


export const forgotPasswordDataProvider = [
    provideState(forgotPasswordFeature),
    provideEffects(ForgotPasswordEffects)
]