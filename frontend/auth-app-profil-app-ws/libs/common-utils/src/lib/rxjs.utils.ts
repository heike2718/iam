import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ResponsePayload } from './common.model';

/**
 * ResponsePayload hat ein möglicherweise undefined generisches Attribut data. Für die Arbeit mit Signals ist es manchmal besser, nichts undefined hineinzupacken.
 */
export function mapResponseDataToType<T>(response$: Observable<ResponsePayload>, undefinedObject: T): Observable<T> {
    return response$.pipe(
        map((response: ResponsePayload) => {
            // Check if 'data' exists and return it as the requested type T, otherwise return undefined
            return response.data ? (response.data as T) : undefinedObject;
        })
    );
}