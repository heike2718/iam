import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LogService } from 'hewi-ng-lib';
import { environment } from '../../environments/environment';

@Injectable({
	providedIn: 'root'
})
export class RemoteValidatorService {

	constructor(private http: HttpClient
		, private logger: LogService) { }


	public validate(what: string, value: string, csrfToken: string) {

		const url = environment.apiUrl + '/validators/' + what;
		const data = {
			input: value
		};

		this.logger.debug('url=' + url + ', data=' + data);

		return this.http.post(url, data, { headers: { 'X-XSRF-TOKEN': csrfToken } });
	}

}
