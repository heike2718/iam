import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { AuthEffects, authFeature } from 'apps/auth-app/src/app/auth/data';

export const authDataProvider = [
    provideState(authFeature),
    provideEffects(AuthEffects)
]