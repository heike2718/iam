import { Injectable } from '@angular/core';
import { fromEvent, map } from 'rxjs';
import { createEffect } from '@ngrx/effects';
import { syncLocalStorage } from '../local-storage.utils';
import { filterDefined } from '@ap-ws/common-utils';

@Injectable()
export class LocalStorageEffects {
  
  storageEvent$ = createEffect(() => {
    return fromEvent<StorageEvent>(window, 'storage').pipe(
      map(x => x ? 'key' : ''),
      filterDefined,
      map((featureState) => syncLocalStorage({ featureState }))
    );
  });
}