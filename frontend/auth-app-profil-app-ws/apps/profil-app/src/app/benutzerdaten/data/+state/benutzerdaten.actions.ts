import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { Benutzerdaten } from "@profil-app/benutzerdaten/model";


export const benutzerdatenActions = createActionGroup({
    source: 'Benutzerdaten',
    events: {
        'LOAD_BENUTZERDATEN': emptyProps(),
        'BENUTZERDATEN_LOADED': props<{ benutzerdaten: Benutzerdaten }>(),
        'RESET_BENUTZERDATEN': emptyProps()
    }
});
