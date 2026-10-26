import { ActionReducer, INIT } from '@ngrx/store';
import { loggedOutEvent } from '@bv-admin/shared/auth/api';

function clearState<S>(reducer: ActionReducer<S>): ActionReducer<S> {
    return (state, action) => {
        if (action != null && action.type === loggedOutEvent.type) {
            return reducer(undefined, { type: INIT });
        }
        return reducer(state, action);
    };
}

export const loggedOutMetaReducer = clearState;
