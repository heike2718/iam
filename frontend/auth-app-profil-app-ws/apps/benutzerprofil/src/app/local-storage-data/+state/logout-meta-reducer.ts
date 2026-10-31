import { ActionReducer, INIT, MetaReducer } from '@ngrx/store';
import { loggedOutEvent } from '../../auth/api/index';

function clearState<S>(reducer: ActionReducer<S>): ActionReducer<S> {
    return (state, action) => {
        if (action != null && action.type === loggedOutEvent.type) {
            return reducer(undefined, { type: INIT });
        }
        return reducer(state, action);
    };
}

export const loggedOutMetaReducer = clearState;
