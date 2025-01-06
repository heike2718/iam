import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { PasswortEffects, passwortFeature } from "@benutzerprofil/passwort/data";

export const passwortDataProvider = [
    provideState(passwortFeature),
    provideEffects(PasswortEffects)
]