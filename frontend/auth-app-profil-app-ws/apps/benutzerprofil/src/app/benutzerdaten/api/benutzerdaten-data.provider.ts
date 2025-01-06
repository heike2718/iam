import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { BenutzerdatenEffects, benutzerdatenFeature } from "@benutzerprofil/benutzerdaten/data";


export const benutzerdatenDataProvider = [
    provideState(benutzerdatenFeature),
    provideEffects(BenutzerdatenEffects)
]