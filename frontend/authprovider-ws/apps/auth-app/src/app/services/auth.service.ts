import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ResponsePayload } from '@authprovider-ws/common-messages';
import { environment } from '../../environments/environment';
import { map, publishLast, refCount } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	constructor(private httpClient: HttpClient) { }


	createAnonymousSession(): Observable<ResponsePayload> {

		const url = environment.apiUrl + '/session';

		return this.httpClient.get(url).pipe(
			map(res => res as ResponsePayload),
			publishLast(),
			refCount()
		);
	}
}
