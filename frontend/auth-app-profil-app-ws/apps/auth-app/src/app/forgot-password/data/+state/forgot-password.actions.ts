import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { TempPasswordCredentials, TempPasswordResponseDto } from "@auth-app/model";


export const forgotPasswordActions = createActionGroup({
    source: 'OrderTempPassword',
    events: {
        'ORDER_TEMP_PASSWORD': props<{ tempPasswordCredentials: TempPasswordCredentials }>(),
        'TEMP_PASSWORD_SUCCESS': props<{ payload: TempPasswordResponseDto }>(),
        'RESET_ORDER_TEMP_PASSWORD_STATE': emptyProps()
    }
});
