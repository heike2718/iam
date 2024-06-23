import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { BenutzerSearchResult, BenutzerSuchparameter, BenutzersucheGUIModel } from "@bv-admin-app/benutzer/model";

export const benutzerActions = createActionGroup({
    source: 'benutzer',
    events: {
        'FIND_BENUTZER': props<{suchparameter: BenutzerSuchparameter}>(),
        'BENUTZER_FOUND': props<{treffer: BenutzerSearchResult}>(),
        'GUI_MODEL_CHANGED': props<{guiModel: BenutzersucheGUIModel}>(),
        'SUCHE_ZURUECKSETZEN': emptyProps(),
        'SELECT_BENUTZER': props<{ benutzerID: string }>(),
        'UNSELECT_BENUTZER': emptyProps()
    }
});
