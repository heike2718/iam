import { store } from '../shared/store/app-data';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';
import { ResponsePayload } from '@authprovider-ws/common-messages';

@Injectable({
	providedIn: 'root'
})
export class AboutService {

	constructor(private httpClient: HttpClient) { }

	loadAboutInfos(): void {

		const url = environment.apiUrl + '/version';

		this.httpClient.get(url).pipe(
			map(res => res as ResponsePayload)).subscribe(
				pl => {
					store.updateApiVersion(pl.message.message);
				},
				(_error => {
					store.updateApiVersion('Der Server ist nicht erreichbar :/');
				}));

	}
}
