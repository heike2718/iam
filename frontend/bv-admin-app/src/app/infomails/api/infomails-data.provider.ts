import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { InfomailsEffects, infomailsFeature } from '@bv-admin-app/infomails/data';



export const infomailsDataProvider = [
    provideState(infomailsFeature),
    provideEffects(InfomailsEffects)
]