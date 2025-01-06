import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { AuthEffects, authFeature } from '@benutzerprofil/auth/data';

export const authDataProvider = [
    provideState(authFeature),
    provideEffects(AuthEffects)
]