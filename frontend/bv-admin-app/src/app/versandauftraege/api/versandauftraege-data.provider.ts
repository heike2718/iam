import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { VersandauftraegeEffects, versandauftraegeFeature } from "@bv-admin-app/versandauftraege/data";



export const versandauftraegeDataProvider = [
    provideState(versandauftraegeFeature),
    provideEffects(VersandauftraegeEffects)
]