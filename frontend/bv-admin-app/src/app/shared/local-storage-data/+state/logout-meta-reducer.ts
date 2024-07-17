import { ActionReducer, INIT, MetaReducer } from '@ngrx/store';
import { loggedOutEvent } from '@bv-admin-app/shared/auth/api';

function clearState(reducer: ActionReducer<any>): ActionReducer<any> {
    return (state, action) => {
        if (action != null && action.type === loggedOutEvent.type) {
            return reducer(undefined, { type: INIT });
        }
        return reducer(state, action);
    };
}

export const loggedOutMetaReducer: MetaReducer<any> = clearState
