import { ActionReducer, INIT, MetaReducer } from '@ngrx/store';
import { loggedOutAction } from '@bv-admin-app/shared/auth/data';

function clearState(reducer: ActionReducer<any>): ActionReducer<any> {
    return (state, action) => {
        if (action != null && action.type === loggedOutAction.type) {
            return reducer(undefined, { type: INIT });
        }
        return reducer(state, action);
    };
}

export const loggedOutMetaReducer: MetaReducer<any> = clearState
