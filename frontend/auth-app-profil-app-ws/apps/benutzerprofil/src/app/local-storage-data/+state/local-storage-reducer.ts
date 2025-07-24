import { ActionReducer } from '@ngrx/store';
import { isSyncLocalStorage, rehydrateApplicationState, localStorageSync } from '../local-storage.utils';

export const localStorageReducer = (...featureStateNames: string[]) => {

  return <S>(reducer: ActionReducer<S>): ActionReducer<S> =>
    (state, action) => {
      if (isSyncLocalStorage(action)) {
        console.log('Hydration-Action erkannt:', action);

        const rehydratedFeatureState = rehydrateApplicationState(
          [action.featureState],
          localStorage,
          (value) => value,
          true
        );
        console.log('Rehydrated State:', rehydratedFeatureState);
        return { ...state, ...rehydratedFeatureState } as S;
      } else {
        console.log('andere Action:', action);
      }
      return localStorageSync({ keys: featureStateNames, rehydrate: true })(reducer)(state, action);
    };
}

