import { MailversandauftragDetails, MailversandauftragOverview, Mailversandgruppe, MailversandgruppeDetails, sortMailversandauftragOverviewByDate } from "@bv-admin-app/versandauftraege/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { versandauftraegeActions } from "./versandauftraege.actions";
import { loggedOutEvent } from "@bv-admin-app/shared/auth/api";
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";
import { Benutzer } from "@bv-admin-app/shared/model";


export interface VersandauftraegeState {
    readonly loaded: boolean;
    readonly versandauftraege: MailversandauftragOverview[];
    readonly details: MailversandauftragDetails[];
    readonly selectedVersandauftrag: MailversandauftragDetails | undefined;
    readonly selectedMailversandgruppe: MailversandgruppeDetails | undefined;
}

const initialVersandauftraegeState: VersandauftraegeState = {
    loaded: false,
    versandauftraege: [],
    details: [],
    selectedVersandauftrag: undefined,
    selectedMailversandgruppe: undefined
}

export const versandauftraegeFeature = createFeature({
    name: 'versandauftraege',
    reducer: createReducer(
        initialVersandauftraegeState,
        on(versandauftraegeActions.vERSANDAUFTRAEGE_LOADED, (state, action) => {
            return { ...state, versandauftraege: action.versandauftraege, loaded: true }
        }),
        on(versandauftraegeActions.vERSANDAUFTRAG_DETAILS_LOADED, (state, action) => {

            if (action.responsePayload.versandauftrag) {

                const theVersandauftrag = action.responsePayload.versandauftrag;
                const filtered = state.details.filter(v => v.uuid === action.responsePayload.uuid);

                const theExistingVersandauftragInAnArray = state.versandauftraege.filter(v => v.uuid === theVersandauftrag.uuid);

                // wenn ich den Versandauftrag nicht gerade gelöscht habe, was dumm wäre, gibt es den Overview!
                const theChangedOverview = { ...theExistingVersandauftragInAnArray[0], status: theVersandauftrag.status };
                const theOtherOverviews = state.versandauftraege.filter(v => v.uuid !== theVersandauftrag.uuid);
                const theChangedVersandauftraege = [...theOtherOverviews, theChangedOverview];

                if (filtered.length === 0) {

                    return {
                        ...state,
                        selectedVersandauftrag: theVersandauftrag,
                        details: [...state.details, action.responsePayload.versandauftrag],
                        versandauftraege: sortMailversandauftragOverviewByDate(theChangedVersandauftraege)
                    };

                } else {
                    const alteDetials = state.details.filter(v => v.uuid !== action.responsePayload.uuid);
                    const neueDetails = [...alteDetials, theVersandauftrag];

                    return {
                        ...state,
                        selectedVersandauftrag:
                            theVersandauftrag,
                        details: neueDetails,
                        versandauftraege: sortMailversandauftragOverviewByDate(theChangedVersandauftraege)
                    };
                }
            } else {

                const neueDetails = state.details.filter(v => v.uuid !== action.responsePayload.uuid);
                const neueVersandauftraege = state.versandauftraege.filter(v => v.uuid !== action.responsePayload.uuid);
                return {
                    ...state,
                    versandauftraege: neueVersandauftraege,
                    details: neueDetails,
                    selectedVersandauftrag: undefined
                };
            }
        }),
        on(versandauftraegeActions.sELECT_VERSANDAUFTRAG, (state, action) => {
            return { ...state, selectedVersandauftrag: action.versandauftrag }
        }),
        on(versandauftraegeActions.uNSELECT_VERSANDAUFTRAG, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return { ...state, selectedVersandauftrag: undefined }
        }),
        on(versandauftraegeActions.vERSANDAUFTRAG_DELETED, (state, action) => {
            const theVersandauftraege = state.versandauftraege.filter(v => v.uuid !== action.responsePayload.uuid);

            return { ...state, selectedVersandauftrag: undefined, versandauftraege: theVersandauftraege }
        }),
        on(versandauftraegeActions.vERSANDGRUPPE_LOADED, (state, action) => {

            if (action.responsePayload.mailversandgruppe) {
                return { ...state, selectedMailversandgruppe: action.responsePayload.mailversandgruppe };
            }

            return { ...state, selectedMailversandgruppe: undefined }
        }),
        on(versandauftraegeActions.vERSANDGRUPPE_BENUTZER_ENTFERNEN, (state, action) => {

            const versandgruppe: MailversandgruppeDetails = action.mailversandgruppe;

            if (state.selectedMailversandgruppe === undefined || state.selectedMailversandgruppe.uuid !== versandgruppe.uuid) {
                return { ...state };
            }

            const removedBenutzer: Benutzer = action.benutzer;
            const remainingBenutzers: Benutzer[] = state.selectedMailversandgruppe.benutzer.filter(b => b.uuid !== removedBenutzer.uuid);
            return { ...state, selectedMailversandgruppe: { ...state.selectedMailversandgruppe, benutzer: remainingBenutzers } };
        }),
        on(versandauftraegeActions.vERSANDAUFTRAG_SCHEDULED, (state, action) => {

            const theNewVersandauftraege = [...state.versandauftraege, action.versandauftrag];
            return { ...state, versandauftraege: sortMailversandauftragOverviewByDate(theNewVersandauftraege) };
        }),
        on(loggedOutEvent, (_state, _action) => {
            swallowEmptyArgument(_action, false);
            return initialVersandauftraegeState;
        }),

    )
})

