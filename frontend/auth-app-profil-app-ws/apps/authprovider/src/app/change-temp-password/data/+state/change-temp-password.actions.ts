import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { ChangeTempPasswordPayload } from "@authprovider/model";
import { ResponsePayload } from "@ap-ws/common-model";


export const changeTempPasswordActions = createActionGroup({
    source: 'ChangeTempPassword',
    events: {
        'CHANGE_TEMP_PASSWORD': props<{ changeTempPasswordPayload: ChangeTempPasswordPayload }>(),
        'TEMP_PASSWORD_CHANGED': props<{ payload: ResponsePayload }>(),
        'RESET_CHANGE_TEMP_PASSWORD_STATE': emptyProps()
    }
});
