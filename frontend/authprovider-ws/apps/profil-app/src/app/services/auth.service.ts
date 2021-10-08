import { Injectable } from '@angular/core';
// tslint:disable-next-line:max-line-length
import { store } from '../shared/store/app-data';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { HttpErrorService } from '../error/http-error.service';
import { map, publishLast, refCount } from 'rxjs/operators';
// tslint:disable-next-line:max-line-length
import { AuthenticatedUser, STORAGE_KEY_FULL_NAME, STORAGE_KEY_SESSION_EXPIRES_AT, STORAGE_KEY_DEV_SESSION_ID, STORAGE_KEY_ID_REFERENCE, AuthResult } from '../shared/model/app-model';
import { Router } from '@angular/router';
import { SessionService } from './session.service';
import { LogService } from '@authprovider-ws/common-logging';
import { ResponsePayload } from '@authprovider-ws/common-messages';

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	constructor(private httpClient: HttpClient
		, private httpErrorService: HttpErrorService
		, private sessionService: SessionService
		, private router: Router
		, private logger: LogService) { }


	logIn() {

		const url = environment.apiUrl + '/auth/login';

		this.httpClient.get(url).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		).subscribe(
			payload => {
				window.location.href = payload.message.message;
			},
			(error => {
				this.httpErrorService.handleError(error, 'logIn');
			}));

	}

	logOut() {

		let url = environment.apiUrl;

		const sessionId = localStorage.getItem(STORAGE_KEY_DEV_SESSION_ID);


		if (sessionId) {
			url = url + '/auth/dev/logout/' + sessionId;
		} else {
			url = url + '/auth/dev/logout/unknown';
		}

		if (environment.production) {
			url = environment.apiUrl + '/auth/logout';
		}

		this.logger.debug('url=' + url);

		this.httpClient.delete(url).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		).subscribe(
			_payload => {
				this.sessionService.clearSession();
			},
			(_error => {
				// ist nicht schlimm: die session bleibt auf dem Server
				this.sessionService.clearSession();
			}));

	}

	createSession(authResult: AuthResult) {

		window.location.hash = '';

		if ('login' === authResult.state) {

			const url = environment.apiUrl + '/auth/session';

			store.updateBlockingIndicator(true);

			this.httpClient.post(url, authResult.idToken).pipe(
				map(res => res as ResponsePayload),
				publishLast(),
				refCount()
			).subscribe(
				payload => {
					if (payload.data) {
						const authUser = payload.data as AuthenticatedUser;

						localStorage.setItem(STORAGE_KEY_FULL_NAME, authUser.session.fullName);
						localStorage.setItem(STORAGE_KEY_SESSION_EXPIRES_AT, JSON.stringify(authUser.session.expiresAt));
						localStorage.setItem(STORAGE_KEY_ID_REFERENCE, authUser.session.idReference);

						if (authUser.session.sessionId && !environment.production) {
							localStorage.setItem(STORAGE_KEY_DEV_SESSION_ID, authUser.session.sessionId);
						}

						store.initUser(authUser.user);
						store.initCsrfToken(authUser.session.csrfToken);
						store.updateBlockingIndicator(false);
						this.router.navigateByUrl('/profil');
					}
				},
				(error => {
					store.updateBlockingIndicator(false);
					this.httpErrorService.handleError(error, 'createSession');
				})
			);



		} else {
			console.log('authResult.state = ' + authResult.state);
		}

	}

	public parseHash(hashStr: string): AuthResult {

		hashStr = hashStr.replace(/^#?\/?/, '');

		const result: AuthResult = {
			expiresAt: 0,
			nonce: undefined,
			state: undefined,
			idToken: undefined
		};

		if (hashStr.length > 0) {

			const tokens = hashStr.split('&');
			tokens.forEach(
				(token) => {
					const keyVal = token.split('=');
					switch (keyVal[0]) {
						case 'expiresAt': result.expiresAt = JSON.parse(keyVal[1]); break;
						case 'nonce': result.nonce = keyVal[1]; break;
						case 'state': result.state = keyVal[1]; break;
						case 'idToken': result.idToken = keyVal[1]; break;
					}
				}
			);
		}
		return result;
	}
}
