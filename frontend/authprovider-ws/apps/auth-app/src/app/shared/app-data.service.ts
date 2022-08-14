import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
// tslint:disable-next-line:max-line-length
import { ClientInformation, ClientCredentials } from './model/auth-model';
import { filter } from 'rxjs/operators';
import * as _ from 'lodash';

@Injectable({
	providedIn: 'root'
})
export class AppData {

	private clientInformationSubject = new BehaviorSubject<ClientInformation>({
		name: '',
		zurueckText: 'zurück',
		agbUrl: '',
		loginnameSupported: false,
		namenRequired: false,
		baseUrl: null
	});

	clientCredentialsSubject = new BehaviorSubject<ClientCredentials>(undefined);

	private redirectUrlSubject = new BehaviorSubject<string>('');

	clientInformation$: Observable<ClientInformation> =
		this.clientInformationSubject.asObservable().pipe(
			filter(client => client.name !== undefined)
		);

	clientCredentials$: Observable<ClientCredentials> =
		this.clientCredentialsSubject.asObservable().pipe(filter(_subj => !!undefined));

	redirectUrl$: Observable<string> = this.redirectUrlSubject.asObservable();

	#loadingSubject = new BehaviorSubject<boolean>(false);

	loading$: Observable<boolean> = this.#loadingSubject.asObservable();

	constructor() { }

	updateClientInformation(clientInformation: ClientInformation) {
		const ci = _.cloneDeep(clientInformation);
		this.clientInformationSubject.next(ci);
	}

	updateClientCredentials(clientCredentials: ClientCredentials) {
		this.clientCredentialsSubject.next(_.cloneDeep(clientCredentials));
	}

	updateRedirectUrl(redirectUrl: string) {
		console.log(redirectUrl);
		this.redirectUrlSubject.next(redirectUrl);
	}

	updateLoading(loading: boolean) {
		this.#loadingSubject.next(loading);
	}
}
