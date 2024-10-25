import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { AuthResult, UserSession } from 'apps/profil-app/src/app/auth/model';

export const authActions = createActionGroup({
    source: 'Auth',
    events: {
        'LOG_IN': emptyProps(),
        'REQUEST_LOGIN_URL': emptyProps(),
        'REQUEST_SIGNUP_URL': emptyProps(),
        'REDIRECT_TO_AUTH': props<{ authUrl: string }>(),
        'CREATE_SESSION': props<{hash: string}>(),
        'INIT_SESSION': props<{ authResult: AuthResult }>(),
        'SESSION_CREATED': props<{ session: UserSession }>(),
        'LOG_OUT': emptyProps(),
        'LOGGED_OUT': emptyProps()
    }

});
