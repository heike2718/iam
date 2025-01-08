import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { ChangeTempPasswordEffects, changeTempPasswordFeature } from "@authprovider/change-temp-password/data";


export const changeTempPasswordDataProvider = [
    provideState(changeTempPasswordFeature),
    provideEffects(ChangeTempPasswordEffects)
]