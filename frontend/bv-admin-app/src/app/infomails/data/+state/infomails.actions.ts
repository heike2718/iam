import { Infomail, UpdateInfomailResponseDto } from "@bv-admin-app/infomails/model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";


export const infomailsActions = createActionGroup({
    source: 'infomails',
    events: {
        'LOAD_INFOMAILS': emptyProps(),
        'INFOMAILS_LOADED': props<{infomails: Infomail[]}>(),
        'INFOMAIL_SELECTED': props<{infomail: Infomail}>(),
        'INFOMAIL_DESELECTED': emptyProps(),
        'INFOMAIL_START_EDIT': emptyProps(),
        'INFOMAIL_CANCEL_EDIT': emptyProps(),
        'INFOMAIL_ADDED': props<{infomail: Infomail}>(),
        'INFOMAIL_UPDATED': props<{responsePayload: UpdateInfomailResponseDto}>(),
        'CLEAR_INFOMAILS': emptyProps()
    }
})