
import { createSelector } from '@ngrx/store';
import { benutzerFeature } from './benutzer.reducer';

const {selectBenutzerState: selectBenutzerState} = benutzerFeature


const guiModel = createSelector(
    selectBenutzerState,
    (state) => state.guiModel
)

const anzahlTreffer = createSelector(
    guiModel,
    (guiModel) => guiModel.anzahlTreffer
)

const paginationState = createSelector(
    guiModel,
    (guiModel) => {guiModel.pageIndex; guiModel.pageSize}
)

const page = createSelector(
    selectBenutzerState,
    (state) => state.page
)

export const fromBenutzer = {
    anzahlTreffer,
    paginationState,
    page,
    guiModel
}
