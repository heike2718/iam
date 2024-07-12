import { MailversandauftragDetails, MailversandauftragOverview } from "@bv-admin-app/versandauftraege/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { versandauftraegeActions } from "./versandauftraege.actions";
import { loggedOutAction } from "@bv-admin-app/shared/auth/data";
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";


export interface VersandauftraegeState {
    readonly versandauftraege: MailversandauftragOverview[];
    readonly selectedVersandauftrag: MailversandauftragDetails | undefined;
}

const initialVersandauftraegeState: VersandauftraegeState = {
    versandauftraege: [],
    selectedVersandauftrag: undefined
}

export const versandauftraegeFeature = createFeature({
    name: 'versandauftraege',
    reducer: createReducer(
        initialVersandauftraegeState,
        on(versandauftraegeActions.vERSANDAUFTRAEGE_LOADED, (state, action) => {
            return { ...state, versandauftraege: action.versandauftraege }
        }),
        on(versandauftraegeActions.vERSANDAUFTRAG_DETAILS_LOADED, (state, action) => {
            return {...state, selectedVersandauftrag: action.versandauftrag}
        }),
        on(versandauftraegeActions.uNSELECT_VERSANDAUFTRAG, (state, action) => {
            return {...state, selectedVersandauftrag: undefined}
        }),
        on(loggedOutAction, (_state, action) => {
            swallowEmptyArgument(action, false);
            return initialVersandauftraegeState;
        })
    )
})

