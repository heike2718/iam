import { computeStatistics, MailversandauftragDetails, MailversandauftragDetailsResponseDto, MailversandauftragOverview, MailversandauftragStatistik, MailversandauftragUIModel, Mailversandgruppe, MailversandgruppeDetails, mapToOverview, sortMailversandauftragOverviewByDate } from "@bv-admin-app/versandauftraege/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { versandauftraegeActions } from "./versandauftraege.actions";
import { loggedOutEvent } from "@bv-admin-app/shared/auth/api";
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";
import { Benutzer } from "@bv-admin-app/shared/model";


export interface VersandauftraegeState {
    readonly loaded: boolean;
    readonly versandauftraege: MailversandauftragOverview[];
    readonly details: MailversandauftragDetails[];
    readonly selectedVersandauftrag: MailversandauftragUIModel | undefined;
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

                const filtered: MailversandauftragDetails[] = state.details.filter(v => v.uuid === action.responsePayload.uuid);
                const theVersandauftrag: MailversandauftragDetails = action.responsePayload.versandauftrag;
                const theExistingVersandauftragInAnArray = state.versandauftraege.filter(v => v.uuid === theVersandauftrag.uuid);

                // wenn ich den Versandauftrag nicht gerade gelöscht habe, was dumm wäre, gibt es den Overview!
                const theChangedOverview = { ...theExistingVersandauftragInAnArray[0], status: theVersandauftrag.status };
                const theOtherOverviews = state.versandauftraege.filter(v => v.uuid !== theVersandauftrag.uuid);
                const theChangedVersandauftraege = [...theOtherOverviews, theChangedOverview];

                if (filtered.length === 0) {

                    return {
                        ...state,
                        selectedVersandauftrag: computeStatistics(theVersandauftrag),
                        details: [...state.details, action.responsePayload.versandauftrag],
                        versandauftraege: sortMailversandauftragOverviewByDate(theChangedVersandauftraege)
                    };

                } else {
                    const alteDetials = state.details.filter(v => v.uuid !== action.responsePayload.uuid);
                    const neueDetails = [...alteDetials, theVersandauftrag];

                    return {
                        ...state,
                        selectedVersandauftrag: computeStatistics(theVersandauftrag),
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

            if (action.versandauftrag) {
                return { ...state, selectedVersandauftrag: computeStatistics(action.versandauftrag) }
            } else {
                return { ...state, selectedVersandauftrag: undefined }
            }
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

                const theVersandauftrag: MailversandauftragUIModel | undefined = state.selectedVersandauftrag;

                if (theVersandauftrag) {

                    const theNewGruppe: Mailversandgruppe = mapToOverview(action.responsePayload.mailversandgruppe, theVersandauftrag.mailversandauftrag.uuid);

                    const newGruppen: Mailversandgruppe[] =
                        theVersandauftrag.mailversandauftrag.mailversandgruppen.map(g => g.uuid === action.responsePayload.mailversandgruppe.uuid ? theNewGruppe : g);

                    const inner: MailversandauftragDetails = { ...theVersandauftrag.mailversandauftrag, mailversandgruppen: newGruppen };

                    return {
                        ...state,
                        selectedMailversandgruppe: action.responsePayload.mailversandgruppe,
                        selectedVersandauftrag: computeStatistics(inner)
                    };
                } else {
                    return { ...state, selectedMailversandgruppe: action.responsePayload.mailversandgruppe, selectedVersandauftrag: undefined }
                }
            } else {
                return { ...state, selectedMailversandgruppe: undefined }
            }

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
        on(versandauftraegeActions.uNSELECT_VERSANDGRUPPE, (state, _action) => {
            return { ...state, selectedMailversandgruppe: undefined };
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

