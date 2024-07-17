import { sortInfomailsByBetreff } from "@bv-admin-app/infomails/model";
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";
import { createFeature, createReducer, on } from "@ngrx/store";
import { loggedOutEvent } from '@bv-admin-app/shared/auth/api';
import { infomailsActions } from './infomails.actions';
import { Infomail } from "@bv-admin-app/shared/model";

export interface InfomailsState {
    readonly infomailsLoaded: boolean;
    readonly infomails: Infomail[];
    readonly selectedInfomail: Infomail | undefined;
    readonly editMode: boolean;
}

const initialInfomailsState: InfomailsState = {
    infomailsLoaded: false,
    infomails: [],
    selectedInfomail: undefined,
    editMode: false
}

export const infomailsFeature = createFeature({
    name: 'infomails',
    reducer: createReducer(
        initialInfomailsState,
        on(infomailsActions.iNFOMAILS_LOADED, (state, action) => {
            
            if (state.selectedInfomail) {
                const theSelectedInfomail = state.selectedInfomail;
                const infomails = action.infomails.filter(i => theSelectedInfomail.uuid === i.uuid);

                if (infomails.length > 0) {
                    return {...state, infomails: action.infomails, infomailsLoaded: true, selectedInfomail: infomails[0]};
                }
            }

            return { ...state, infomails: action.infomails, infomailsLoaded: true }
        }),
        on(infomailsActions.iNFOMAIL_SELECTED, (state, action) => {
            return { ...state, selectedInfomail: action.infomail, editMode: false }
        }),
        on(infomailsActions.iNFOMAIL_DESELECTED, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return { ...state, selectedInfomail: undefined, editMode: false }
        }),
        on(infomailsActions.iNFOMAIL_START_EDIT, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return { ...state, editMode: true }
        }),
        on(infomailsActions.iNFOMAIL_CANCEL_EDIT, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return { ...state, editMode: false }
        }),
        on(infomailsActions.cLEAR_INFOMAILS, (_state, _action) => {
            swallowEmptyArgument(_action, false);
            return initialInfomailsState;
        }),
        on(infomailsActions.iNFOMAIL_ADDED, (state, action) => {

            const theNewInfomails = [...state.infomails, action.infomail];
            return { ...state, infomails: sortInfomailsByBetreff(theNewInfomails) };
        }),
        on(infomailsActions.iNFOMAIL_CHANGED, (state, action) => {

            if (action.responsePayload.infomail) {

                const theChangedInfoMail: Infomail = action.responsePayload.infomail;

                if (state.selectedInfomail) {
                    return {
                        ...state,
                        selectedInfomail: theChangedInfoMail,
                        infomails: sortInfomailsByBetreff(state.infomails.map((im: Infomail) => im.uuid === theChangedInfoMail.uuid ? theChangedInfoMail : im))
                    };
                } else {
                    return {
                        ...state,
                        infomails: sortInfomailsByBetreff(state.infomails.map((im: Infomail) => im.uuid === theChangedInfoMail.uuid ? theChangedInfoMail : im))
                    };
                }
            } else {
                return {
                    ...state,
                    infomails: state.infomails.filter(im => action.responsePayload.uuid !== im.uuid),
                    selectedInfomail: undefined,
                    editMode: false
                };
            }
        }),
        on(loggedOutEvent, (_state, _action) => {
            swallowEmptyArgument(_action, false);
            return initialInfomailsState;
        })

    )
})

