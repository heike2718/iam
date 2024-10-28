import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { BenutzerdatenEffects, benutzerdatenFeature } from "@profil-app/benutzerdaten/data";


export const benutzerdatenDataProvider = [
    provideState(benutzerdatenFeature),
    provideEffects(BenutzerdatenEffects)
]