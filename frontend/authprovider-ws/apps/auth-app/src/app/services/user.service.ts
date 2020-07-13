import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegistrationCredentials, AuthSession } from '../shared/model/auth-model';
import { HttpErrorService } from '../error/http-error.service';
import { environment } from '../../environments/environment';
import { createHash } from '../shared/model/auth-response-data';
import { map, publishLast, refCount, tap } from 'rxjs/operators';
import { ResponsePayload, LogService } from 'hewi-ng-lib';
import { AppData } from '../shared/app-data.service';
import { LoginCredentials } from '../shared/model/auth-model';
import { SessionService } from './session.service';

@Injectable({
	providedIn: 'root'
})
export class UserService {

	constructor(private http: HttpClient
		, private httpErrorService: HttpErrorService
		, private sessionService: SessionService
		, private appData: AppData
		, private logger: LogService) { }

	public registerUser(registrationCredentials: RegistrationCredentials, session: AuthSession): void {

		this.logger.debug('registerUser: start');

		// const headers = new Headers(); headers.append('Content-Type', 'application/json');
		const url = environment.apiUrl + '/users/signup';

		// Bei Erfolg: ReponsePayload mit INFO-Message
		const redirectUrl = registrationCredentials.clientCredentials.redirectUrl;

		this.http.post(url, registrationCredentials, { headers: { 'X-XSRF-TOKEN': session.csrfToken } }).pipe(
			map(res => <ResponsePayload>res),
			publishLast(),
			refCount(),
			tap(
				() => this.logger.debug('inside pipe')
			)
		).subscribe(
			payload => {
				this.sessionService.clearSession();
				this.appData.updateRedirectUrl(redirectUrl + createHash(payload.data));
			},
			error => this.httpErrorService.handleError(error, 'registerUser', undefined),
			() => this.logger.debug('post call completed')
		);
	}

	public loginUser(loginCredentials: LoginCredentials, session: AuthSession): void {

		this.logger.debug('loginUser: start');

		// const url = environment.apiUrl + '/auth/sessions';
		const url = environment.apiUrl + '/auth/sessions/auth-token-grant';

		// Bei Erfolg: ReponsePayload mit INFO-Message
		const redirectUrl = loginCredentials.clientCredentials.redirectUrl;

		this.http.post(url, loginCredentials, { headers: { 'X-XSRF-TOKEN': session.csrfToken } }).pipe(
			map(res => <ResponsePayload>res),
			publishLast(),
			refCount(),
			tap(
				() => this.logger.debug('inside pipe')
			)
		).subscribe(
			payload => {
				this.sessionService.clearSession();
				this.appData.updateRedirectUrl(redirectUrl + createHash(payload.data));
			},
			error => this.httpErrorService.handleError(error, 'registerUser', undefined),
			() => this.logger.debug('post call completed')
		);
	}
}

