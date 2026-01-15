import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { AuthEffects } from '../data/+state/auth.effects';
import { authFeature } from '../data/+state/auth.reducer';

export const authDataProvider = [
    provideState(authFeature),
    provideEffects(AuthEffects)
]