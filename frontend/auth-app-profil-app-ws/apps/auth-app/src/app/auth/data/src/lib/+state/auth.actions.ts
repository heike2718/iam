import { AuthSession } from '@auth-app/auth/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const authActions = createActionGroup({
    source: 'auth',
    events: {
        'CREATE_ANONYMOUS_SESSION': emptyProps(),
        'ANONYMOUS_SESSION_CREATED': props<{authSession: AuthSession}>(),
        'CLEAR_SESSION': emptyProps()
    }
});
