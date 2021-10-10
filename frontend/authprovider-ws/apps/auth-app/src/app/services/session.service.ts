import { Injectable } from '@angular/core';
import { STORAGE_KEY_DEV_SESSION_ID, STORAGE_KEY_SESSION_EXPIRES_AT, AuthSession } from '../shared/model/auth-model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { map, publishLast, refCount } from 'rxjs/operators';
import { ResponsePayload } from '@authprovider-ws/common-messages';

@Injectable({
	providedIn: 'root'
})
export class SessionService {

	constructor(private httpClient: HttpClient) { }

	public clearSession() {

		let url = '';

		const sessionId = localStorage.getItem(STORAGE_KEY_DEV_SESSION_ID);

		if (sessionId) {
			url = environment.apiUrl + '/session/dev/invalidate/' + sessionId;
		} else {
			url = environment.apiUrl + '/session/invalidate';
		}

		this.httpClient.delete(url).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		).subscribe(
			_payload => {},
			(_error => {
				// ist nicht schlimm: die session bleibt auf dem Server
			}));

		localStorage.removeItem(STORAGE_KEY_DEV_SESSION_ID);
		localStorage.removeItem(STORAGE_KEY_SESSION_EXPIRES_AT);

	}

	public setSession(authSession: AuthSession) {

		if (authSession) {
			localStorage.setItem(STORAGE_KEY_SESSION_EXPIRES_AT, JSON.stringify(authSession.expiresAt));

			if (authSession.sessionId && !environment.production) {
				localStorage.setItem(STORAGE_KEY_DEV_SESSION_ID, authSession.sessionId);
			}
		}
	}
}
