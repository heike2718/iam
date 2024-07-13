import { Infomail } from "@bv-admin-app/infomails/model";
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";
import { createFeature, createReducer, on } from "@ngrx/store";
import { loggedOutAction } from '@bv-admin-app/shared/auth/data';
import { infomailsActions } from './infomails.actions';

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
            return {...state, infomails: action.infomails, infomailsLoaded: true}
        }),
        on(infomailsActions.iNFOMAIL_SELECTED, (state, action) => {
            return {...state, selectedInfomail: action.infomail, editMode: false}
        }),  
        on(infomailsActions.iNFOMAIL_DESELECTED, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return {...state, selectedInfomail: undefined, editMode: false}
        }),
        on(infomailsActions.iNFOMAIL_START_EDIT, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return {...state, editMode: true}
        }),    
        on(infomailsActions.iNFOMAIL_CANCEL_EDIT, (state, _action) => {
            swallowEmptyArgument(_action, false);
            return {...state, editMode: false}
        }),    
        on(infomailsActions.cLEAR_INFOMAILS, (_state, _action) => {
            swallowEmptyArgument(_action, false);
            return initialInfomailsState;
        }),       
        on(loggedOutAction, (_state, _action) => {
            swallowEmptyArgument(_action, false);
            return initialInfomailsState;
        })

    )
})

