import { ActionReducer } from '@ngrx/store';
import {
  localStorageSync,
  rehydrateApplicationState,
} from 'ngrx-store-localstorage';
import { isSyncLocalStorage } from '../local-storage.utils';


export const localStorageReducer = (...featureStateNames: string[]) => {
  
  const syncerFn = localStorageSync({
    keys: featureStateNames,
    rehydrate: true,
  });

  return <S>(reducer: ActionReducer<S>): ActionReducer<S> =>
    (state, action) => {
      if (isSyncLocalStorage(action)) {

        // console.log('syncronize local storage');

        const rehydratedFeatureState = rehydrateApplicationState(
          [action.featureState],
          localStorage,
          (value) => value,
          true
        );
        return { ...state, ...rehydratedFeatureState };
      }
      return syncerFn(reducer)(state, action);
    };
}

