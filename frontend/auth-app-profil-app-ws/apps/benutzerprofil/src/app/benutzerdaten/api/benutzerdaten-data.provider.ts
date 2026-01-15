import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { BenutzerdatenEffects } from '../data/+state/benutzerdaten.effects';
import { benutzerdatenFeature } from '../data/+state/benutzerdaten.reducer';


export const benutzerdatenDataProvider = [
    provideState(benutzerdatenFeature),
    provideEffects(BenutzerdatenEffects)
]