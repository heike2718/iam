import { createSelector } from '@ngrx/store';
import { infomailsFeature } from './infomails.reducer';

const {selectInfomailsState: selectInfomailsState} = infomailsFeature

const infomailsLoaded = createSelector(
    selectInfomailsState,
    (state) => state.infomailsLoaded
)

const infomails = createSelector(
    selectInfomailsState,
    (state) => state.infomails
)

const selectedInfomail = createSelector(
    selectInfomailsState,
    (state) => state.selectedInfomail
)


export const fromInfomails = {
    infomailsLoaded,
    infomails,
    selectedInfomail
}