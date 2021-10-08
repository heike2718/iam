import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, publishLast, refCount } from 'rxjs/operators';
import { store } from '../shared/store/app-data';
import { HttpErrorService } from '../error/http-error.service';
import { environment } from '../../environments/environment';
import { User
	, ChangePasswordPayload
	, ProfileDataPayload
	, AuthenticatedUser
	, STORAGE_KEY_FULL_NAME
	, STORAGE_KEY_SESSION_EXPIRES_AT
	, STORAGE_KEY_DEV_SESSION_ID } from '../shared/model/app-model';
import { Observable } from 'rxjs';
import { Message, MessageService, ResponsePayload } from '@authprovider-ws/common-messages';
import { LogService } from '@authprovider-ws/common-logging';

@Injectable({
	providedIn: 'root'
})
export class UserService {

	constructor(private http: HttpClient
		, private httpErrorService: HttpErrorService
		, private messageService: MessageService
		, private logService: LogService) { }


	resetUser(user: User): void {
		store.initUser(user);
	}

	loadUser(): void {
		const url = environment.apiUrl + '/profiles/profile';
		store.updateBlockingIndicator(true);

		this.http.get(url).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		).subscribe(
			payload => {
				const authUser = payload.data as AuthenticatedUser;

				if (authUser.session.fullName !== null) {
					localStorage.setItem(STORAGE_KEY_FULL_NAME, authUser.session.fullName);
				}
				localStorage.setItem(STORAGE_KEY_SESSION_EXPIRES_AT, JSON.stringify(authUser.session.expiresAt));

				if (authUser.session.sessionId && !environment.production) {
					localStorage.setItem(STORAGE_KEY_DEV_SESSION_ID, authUser.session.sessionId);
				}

				store.initUser(authUser.user);
				store.updateBlockingIndicator(false);
			},
			(error => {
				store.updateBlockingIndicator(false);
				this.httpErrorService.handleError(error, 'changePassword');
			})
		);
	}

	changePassword(changePasswordPayload: ChangePasswordPayload, cachedUser: User, csrfToken: string): void {

		const url = environment.apiUrl + '/profiles/profile/password';
		store.updateBlockingIndicator(true);

		this.http.put(url, changePasswordPayload, { headers: { 'X-XSRF-TOKEN': csrfToken } }).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		).subscribe(
			payload => {
				if (payload !== null) {
					if (payload.data) {
						const authUser = payload.data as AuthenticatedUser;
						localStorage.setItem(STORAGE_KEY_SESSION_EXPIRES_AT, JSON.stringify(authUser.session.expiresAt));

						if (authUser.session.sessionId && !environment.production) {
							localStorage.setItem(STORAGE_KEY_DEV_SESSION_ID, authUser.session.sessionId);
						}
					}
					if (payload.message) {
						const _message: Message = payload.message;
						this.messageService.info(_message.message);
					}
					store.updateBlockingIndicator(false);
					// this.sessionService.clearSession();

				} else {
					store.updateBlockingIndicator(false);
					this.messageService.error('Es ist ein unerwarteter Fehler aufgetreten. Bitte schreiben Sie eine Mail an info@egladil.de.');
					this.logService.error('changeProfileData: response payload war null');
					store.initUser(cachedUser);
				}

			},
			(error => {
				store.updateBlockingIndicator(false);
				this.httpErrorService.handleError(error, 'changePassword');
			})
		);
	}

	changeProfileData(profileDataPayload: ProfileDataPayload, cachedUser: User, csrfToken: string): void {

		const url = environment.apiUrl + '/profiles/profile/data';
		store.updateBlockingIndicator(true);

		this.http.put(url, profileDataPayload, { headers: { 'X-XSRF-TOKEN': csrfToken } }).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		).subscribe(
			payload => {
				if (payload !== null) {
					if (payload.data) {
						const authUser = payload.data as AuthenticatedUser;

						if (authUser.session.fullName !== null) {
							localStorage.setItem(STORAGE_KEY_FULL_NAME, authUser.session.fullName);
						}
						localStorage.setItem(STORAGE_KEY_SESSION_EXPIRES_AT, JSON.stringify(authUser.session.expiresAt));

						if (authUser.session.sessionId && !environment.production) {
							localStorage.setItem(STORAGE_KEY_DEV_SESSION_ID, authUser.session.sessionId);
						}

						store.initUser(authUser.user);
					}
					if (payload.message) {
						const _message: Message = payload.message;
						this.messageService.info(_message.message);
					}
					store.updateBlockingIndicator(false);
				} else {
					store.updateBlockingIndicator(false);
					this.messageService.error('Es ist ein unerwarteter Fehler aufgetreten. Bitte schreiben Sie eine Mail an info@egladil.de.');
					this.logService.error('changeProfileData: response payload war null');
					store.initUser(cachedUser);
				}

			},
			(error => {
				store.updateBlockingIndicator(false);
				this.httpErrorService.handleError(error, 'changeProfileData');
			})
		);
	}

	deleteAccount(csrfToken: string): Observable<ResponsePayload> {
		const url = environment.apiUrl + '/profiles/profile';
		store.updateBlockingIndicator(true);

		return this.http.delete(url, { headers: { 'X-XSRF-TOKEN': csrfToken } }).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		);
	}
}
