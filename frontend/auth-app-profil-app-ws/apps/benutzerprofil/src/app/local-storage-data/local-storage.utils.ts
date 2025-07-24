import { Action } from '@ngrx/store';
import { createAction, props } from '@ngrx/store';

export const syncLocalStorage = createAction(
  '[benutzerdaten] Sync Local Store',
  props<{ featureState: string }>()
);

export function isSyncLocalStorage(
  action: Action
): action is ReturnType<typeof syncLocalStorage> {
  return action.type === syncLocalStorage.type;
}

/**
 * Reads the specified feature states from localStorage and returns them as an object.
 * @param featureStateNames Array of feature state keys to rehydrate.
 * @param storage Storage object (usually window.localStorage).
 * @param reviver Optional reviver function for JSON.parse.
 * @param merge If true, returns a merged object; otherwise, returns the raw value.
 */
export function rehydrateApplicationState(
  featureStateNames: string[],
  storage: Storage,
  reviver?: (key: string, value: any) => any,
  merge: boolean = true
): Record<string, any> {
  const result: Record<string, any> = {};

  for (const key of featureStateNames) {
    const stored = storage.getItem(key);
    if (stored !== null) {
      try {
        result[key] = JSON.parse(stored, reviver);
      } catch {
        // Ignore parse errors
      }
    }
  }
  return merge ? result : Object.values(result);
}

/**
 * Syncs specified feature states to localStorage after each action.
 * @param options Object with keys (feature state names) and rehydrate flag.
 */
export function localStorageSync(options: { keys: string[], rehydrate?: boolean }) {
  return <S>(reducer: (state: S | undefined, action: any) => S) =>
    (state: S | undefined, action: any): S => {
      const nextState = reducer(state, action);

      for (const key of options.keys) {
        if (nextState && Object.prototype.hasOwnProperty.call(nextState, key)) {
          try {
            localStorage.setItem(key, JSON.stringify((nextState as any)[key]));
          } catch {
            // Ignore storage errors
          }
        }
      }

      return nextState;
    };
}