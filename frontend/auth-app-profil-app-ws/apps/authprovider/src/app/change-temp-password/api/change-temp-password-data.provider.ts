import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { ChangeTempPasswordEffects } from '../data/+state/change-temp-password.effects';
import { changeTempPasswordFeature } from '../data/+state/change-temp-password.reducer';


export const changeTempPasswordDataProvider = [
    provideState(changeTempPasswordFeature),
    provideEffects(ChangeTempPasswordEffects)
]