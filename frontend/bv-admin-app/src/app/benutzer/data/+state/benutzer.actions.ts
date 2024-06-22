import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { BenutzerSearchResult, BenutzerSuchparameter, BenutzerTableFilter, PageDefinition, PaginationState } from "@bv-admin-app/benutzer/model";

export const benutzerActions = createActionGroup({
    source: 'benutzer',
    events: {
        'FIND_BENUTZER': props<{suchparameter: BenutzerSuchparameter}>(),
        'BENUTZER_FOUND': props<{treffer: BenutzerSearchResult}>(),
        'PAGINATION_STATE_CHANGED': props<{paginationState: PaginationState}>(),
        'BENUTZER_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'BENUTZER_TABLE_FILTER_CHANGED': props<{suchparameter: BenutzerTableFilter}>(),
        'SELECT_BENUTZER': props<{ benutzerID: string }>(),
        'UNSELECT_BENUTZER': emptyProps()
    }
});
