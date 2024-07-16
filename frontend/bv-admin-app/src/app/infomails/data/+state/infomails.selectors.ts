import { createSelector } from '@ngrx/store';
import { infomailsFeature } from './infomails.reducer';

const {selectInfomailsState: infomailsState} = infomailsFeature

const infomailsLoaded = createSelector(
    infomailsState,
    (state) => state.infomailsLoaded
)

const infomails = createSelector(
    infomailsState,
    (state) => state.infomails
)

const selectedInfomail = createSelector(
    infomailsState,
    (state) => state.selectedInfomail
)

const editMode = createSelector(
    infomailsState,
    (state) => state.editMode
)


export const fromInfomails = {
    infomailsLoaded,
    infomails,
    selectedInfomail,
    editMode
}