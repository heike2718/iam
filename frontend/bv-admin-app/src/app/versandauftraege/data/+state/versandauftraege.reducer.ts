import { MailversandauftragDetails, MailversandauftragOverview, sortMailversandauftragOverviewByBetreff } from "@bv-admin-app/versandauftraege/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { versandauftraegeActions } from "./versandauftraege.actions";
import { loggedOutEvent } from "@bv-admin-app/shared/auth/api";
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";


export interface VersandauftraegeState {
    readonly loaded: boolean;
    readonly versandauftraege: MailversandauftragOverview[];
    readonly details: MailversandauftragDetails[];
    readonly selectedVersandauftrag: MailversandauftragDetails | undefined;
}

const initialVersandauftraegeState: VersandauftraegeState = {
    loaded: false,
    versandauftraege: [],
    details: [],
    selectedVersandauftrag: undefined
}

export const versandauftraegeFeature = createFeature({
    name: 'versandauftraege',
    reducer: createReducer(
        initialVersandauftraegeState,
        on(versandauftraegeActions.vERSANDAUFTRAEGE_LOADED, (state, action) => {
            return { ...state, versandauftraege: action.versandauftraege, loaded: true }
        }),
        on(versandauftraegeActions.vERSANDAUFTRAG_DETAILS_LOADED, (state, action) => {

            // TODO: Liste aktualisiert sich nicht, wenn ein Versandauftrag refreshed wurde und dann finished ist

            if (action.responsePayload.versandauftrag) {

                const filtered = state.details.filter(v => v.uuid === action.responsePayload.uuid);

                if (filtered.length === 0) {
                    return { ...state, selectedVersandauftrag: action.responsePayload.versandauftrag, details: [...state.details, action.responsePayload.versandauftrag] };
                } else {
                    return { ...state, selectedVersandauftrag: action.responsePayload.versandauftrag };
                }
            }

            const neueDetails = state.details.filter(v => v.uuid !== action.responsePayload.uuid);
            const neueVersandauftraege = state.versandauftraege.filter(v => v.uuid !== action.responsePayload.uuid);
            return { ...state, versandauftraege: neueVersandauftraege, details: neueDetails, selectedVersandauftrag: undefined };
        }),
        on(versandauftraegeActions.sELECT_VERSANDAUFTRAG, (state, action) => {
            return { ...state, selectedVersandauftrag: action.versandauftrag }
        }),
        on(versandauftraegeActions.uNSELECT_VERSANDAUFTRAG, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return { ...state, selectedVersandauftrag: undefined }
        }),
        on(versandauftraegeActions.vERSANDAUFTRAG_SCHEDULED, (state, action) => {

            const theNewVersandauftraege = [...state.versandauftraege, action.versandauftrag];
            return { ...state, versandauftraege: sortMailversandauftragOverviewByBetreff(theNewVersandauftraege) };
        }),
        on(loggedOutEvent, (_state, _action) => {
            swallowEmptyArgument(_action, false);
            return initialVersandauftraegeState;
        })
    )
})

