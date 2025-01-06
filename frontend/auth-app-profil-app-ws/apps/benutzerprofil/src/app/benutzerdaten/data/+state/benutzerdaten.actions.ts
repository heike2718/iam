import { Message } from "@ap-ws/common-model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { Benutzerdaten, ChangeBenutzerdatenResponseDto } from "@benutzerprofil/benutzerdaten/model";


export const benutzerdatenActions = createActionGroup({
    source: 'Benutzerdaten',
    events: {
        'LOAD_BENUTZERDATEN': emptyProps(),
        'BENUTZERDATEN_LOADED': props<{ benutzerdaten: Benutzerdaten }>(),
        'BENUTZERDATEN_AENDERN': props<{ benutzerdaten: Benutzerdaten }>(),
        'BENUTZERDATEN_GEAENDERT': props<{ responseDto: ChangeBenutzerdatenResponseDto }>(),        
        'RESET_BENUTZERDATEN': emptyProps(),
        'KONTO_LOESCHEN': emptyProps(),
        'KONTO_GELOESCHT': props<{message: Message}>()
    }
});
