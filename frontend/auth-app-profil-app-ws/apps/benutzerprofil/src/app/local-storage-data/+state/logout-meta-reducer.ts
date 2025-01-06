import { ActionReducer, INIT, MetaReducer } from '@ngrx/store';
import { loggedOutEvent } from '@benutzerprofil/auth/api';

function clearState(reducer: ActionReducer<any>): ActionReducer<any> {
    
    return (state, action) => {
        if (action != null && action.type === loggedOutEvent.type) {
            return reducer(undefined, { type: INIT });
        }
        return reducer(state, action);
    };
}

export const loggedOutMetaReducer: MetaReducer<any> = clearState
