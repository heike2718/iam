import { Infomail, InfomailRequestDto, UpdateInfomailResponseDto } from "@bv-admin-app/infomails/model";
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
        'ADD_INFOMAIL': props<{infomailRequestDto: InfomailRequestDto}>(),
        'INFOMAIL_ADDED': props<{infomail: Infomail}>(),        
        'UPDATE_INFOMAIL': props<{uuid: string, infomailRequestDto: InfomailRequestDto}>(),
        'INFOMAIL_CHANGED': props<{responsePayload: UpdateInfomailResponseDto}>(),        
        'CLEAR_INFOMAILS': emptyProps()
    }
})