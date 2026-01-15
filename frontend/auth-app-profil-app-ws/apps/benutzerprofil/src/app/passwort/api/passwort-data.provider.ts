import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { PasswortEffects } from '../data/+state/passwort.effects';
import { passwortFeature } from '../data/+state/passwort.reducer';

export const passwortDataProvider = [
    provideState(passwortFeature),
    provideEffects(PasswortEffects)
]