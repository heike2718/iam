import { createSelector } from '@ngrx/store';
import { versandauftraegeFeature } from './versandauftraege.reducer';

const {selectVersandauftraegeState: versandauftraegeState} = versandauftraegeFeature;

const versandauftraege = createSelector(
    versandauftraegeState,
    (state) => state.versandauftraege
)

export const fromVersandauftraege = {
    versandauftraege
}

