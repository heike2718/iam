import { Message, PasswortPayload } from "@ap-ws/common-model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";

export const passwortActions = createActionGroup({
    source: 'Passwort',
    events: {
        'LOCALLY_STORE_PASSWORT':  props<{ passwortPayload: PasswortPayload }>(),
        'PASSWORT_AENDERN': props<{ passwortPayload: PasswortPayload }>(),
        'PASSWORT_GEAENDERT': props<{ message: Message }>(),        
        'RESET_PASSWORT': emptyProps()
    }
});