import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { map, Observable } from "rxjs";
import { mapResponseDataToType } from '@ap-ws/common-utils';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { anonymousSession, AuthResult, UserSession } from "apps/profil-app/src/app/auth/model";
import { Message, ResponsePayload } from "@ap-ws/common-model";


@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #httpClient = inject(HttpClient);
    #url = '/profil-api';


    public getLoginUrl(): Observable<Message> {

        const url =  this.#url + '/auth/login';
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#httpClient.get<ResponsePayload>(url, { headers }).pipe(
            map((rp) => rp.message)
        );
    }

    public createSession(authResult: AuthResult): Observable<UserSession> {

        const url = this.#url + '/auth/session';

        const obs$: Observable<ResponsePayload> = this.#httpClient.post<ResponsePayload>(url, authResult.idToken);
        return mapResponseDataToType<UserSession>(obs$, anonymousSession);

    }


    public logOut(): Observable<ResponsePayload> {

        // const url = sessionId === undefined ? this.#configuration.baseUrl + '/auth/logout' : this.#configuration.baseUrl + '/auth/dev/logout/' + sessionId;
        const url = this.#url + '/auth/logout';

        return this.#httpClient.delete<ResponsePayload>(url);


    }

}
