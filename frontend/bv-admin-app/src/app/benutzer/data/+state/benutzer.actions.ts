import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { Benutzer, BenutzerSearchResult, BenutzerSuchparameter, BenutzersucheFilterValues } from "@bv-admin-app/benutzer/model";
import { PageDefinition } from '@bv-admin-app/shared/model'

export const benutzerActions = createActionGroup({
    source: 'benutzer',
    events: {
        'FIND_BENUTZER': props<{suchparameter: BenutzerSuchparameter}>(),
        'BENUTZER_FOUND': props<{treffer: BenutzerSearchResult}>(),
        'BENUTZER_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'BENUTZER_FILTER_CHANGED': props<{filter: BenutzersucheFilterValues}>(),
        'TABLE_BENUTZERSELECTION_CHANGED': props<{selection: Benutzer[]}>(),
        'RESET_FILTER': emptyProps(),
        // 'SELECT_BENUTZER': props<{ benutzerID: string }>(),
        // 'UNSELECT_BENUTZER': emptyProps()
    }
});
