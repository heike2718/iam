import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { map, Observable } from "rxjs";
import { AuthSession, undefinedAuthSession } from "apps/auth-app/src/app/auth/model";
import { AuthAppConfiguration } from '../../config/auth-app.configuration';
import { ResponsePayload, mapResponseDataToType } from '@ap-ws/common-utils';


@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #httpClient = inject(HttpClient);
    #configuration = inject(AuthAppConfiguration)

    public createAnonymousSession(): Observable<AuthSession> {

        const url = this.#configuration.baseUrl + '/session';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        const obs$: Observable<ResponsePayload> = this.#httpClient.get<ResponsePayload>(url, { headers: headers });
        return mapResponseDataToType<AuthSession>(obs$, undefinedAuthSession);
    }

}