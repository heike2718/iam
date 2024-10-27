import { Action } from '@ngrx/store';
import { createAction, props } from '@ngrx/store';



export const syncLocalStorage = createAction(
  '[profil-app] Sync Local Store',
  props<{ featureState: string }>()
);

export function isSyncLocalStorage(
  action: Action
): action is ReturnType<typeof syncLocalStorage> {
  return action.type === syncLocalStorage.type;
}
