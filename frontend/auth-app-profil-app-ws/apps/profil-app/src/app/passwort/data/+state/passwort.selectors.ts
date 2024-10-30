import { createSelector } from '@ngrx/store';
import { passwortFeature } from './passwort.reducer';

const { selectPasswortState } = passwortFeature;


const passwort = createSelector(
    selectPasswortState,
    (state) => state.passwortPayload
)

export const fromPasswort = {
    passwort
}