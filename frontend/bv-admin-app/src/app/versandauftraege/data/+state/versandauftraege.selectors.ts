import { createSelector } from '@ngrx/store';
import { versandauftraegeFeature } from './versandauftraege.reducer';

const {selectVersandauftraegeState: versandauftraegeState} = versandauftraegeFeature;

const loaded = createSelector(
    versandauftraegeState,
    (state) => state.loaded
);

const versandauftraege = createSelector(
    versandauftraegeState,
    (state) => state.versandauftraege
)

const selectedVersandauftrag = createSelector(
    versandauftraegeState,
    (state) => state.selectedVersandauftrag
)

const versandauftraegeDetails = createSelector(
    versandauftraegeState,
    (state) => state.details
)

const selectedMailversandgruppe = createSelector(
    versandauftraegeState,
    (state) => state.selectedMailversandgruppe

)


export const fromVersandauftraege = {
    loaded,
    versandauftraege,
    versandauftraegeDetails,
    selectedVersandauftrag,
    selectedMailversandgruppe
}

