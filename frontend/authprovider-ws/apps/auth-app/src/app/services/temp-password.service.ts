import { Injectable } from '@angular/core';
import { TempPasswordCredentials, ChangeTempPasswordPayload, AuthSession } from '../shared/model/auth-model';
import { map, publishLast, refCount, tap } from 'rxjs/operators';
import { ResponsePayload } from '@authprovider-ws/common-messages';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { LogService } from '@authprovider-ws/common-logging';

@Injectable({
	providedIn: 'root'
})
export class TempPasswordService {

	private url = environment.apiUrl + '/temppwd';


	constructor(private http: HttpClient
		, private logger: LogService) { }

	orderTempPassword(tempPasswordCredentials: TempPasswordCredentials, session: AuthSession): Observable<ResponsePayload> {

		this.logger.debug('orderTempPassword: start');

		return this.http.post(this.url, tempPasswordCredentials, { headers: { 'X-XSRF-TOKEN': session.csrfToken } }).pipe(
			map(res => <ResponsePayload>res),
			publishLast(),
			refCount(),
			tap(
				() => this.logger.debug('orderTempPassword: inside pipe')
			)
		);
	}

	changeTempPassword(changeTempPasswordPayload: ChangeTempPasswordPayload, session: AuthSession): Observable<ResponsePayload> {


		this.logger.debug('changeTempPasswordPayload: start');

		return this.http.put(this.url, changeTempPasswordPayload, { headers: { 'X-XSRF-TOKEN': session.csrfToken } }).pipe(
			map(res => <ResponsePayload>res),
			publishLast(),
			refCount(),
			tap(
				() => this.logger.debug('orderTempPassword: inside pipe')
			)
		);
	}
}


