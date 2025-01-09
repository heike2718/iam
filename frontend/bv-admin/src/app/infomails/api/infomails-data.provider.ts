import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { InfomailsEffects, infomailsFeature } from '@bv-admin/infomails/data';



export const infomailsDataProvider = [
    provideState(infomailsFeature),
    provideEffects(InfomailsEffects)
]