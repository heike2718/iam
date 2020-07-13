import { Injectable } from '@angular/core';
import { AuthSession } from '../shared/model/auth-model';
import { LogService } from 'hewi-ng-lib';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';


@Injectable({
	providedIn: 'root'
})
export class SignupValidationService {

	constructor(private http: HttpClient
		, private logger: LogService) { }


	public validate(what: string, value: string, session: AuthSession) {

		const url = environment.apiUrl + '/validators/' + what;
		const data = {
			'input': value
		};

		this.logger.debug('url=' + url + ', data=' + data);

		return this.http.post(url, data, { headers: { 'X-XSRF-TOKEN': session.csrfToken } });
	}
}

