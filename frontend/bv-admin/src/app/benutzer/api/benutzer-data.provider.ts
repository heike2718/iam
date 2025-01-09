import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { BenutzerEffects, benutzerFeature } from '@bv-admin/benutzer/data';



export const benutzerDataProvider = [
    provideState(benutzerFeature),
    provideEffects(BenutzerEffects)
]