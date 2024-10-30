import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { PasswortEffects, passwortFeature } from "@profil-app/passwort/data";

export const passwortDataProvider = [
    provideState(passwortFeature),
    provideEffects(PasswortEffects)
]