import { inject, Injectable, ɵsetInjectorProfilerContext } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { map, Observable } from "rxjs";
import { Message, ResponsePayload, mapResponseDataToType } from '@ap-ws/common-utils';
import { ProfilAppConfiguration } from '../../../../configuration/profil-app.configuration';
import { anonymousSession, AuthResult, UserSession } from "@profil-app/auth/model";


@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #configuration = inject(ProfilAppConfiguration);
    #httpClient = inject(HttpClient);


    public getLoginUrl(): Observable<Message> {

        const url = this.#configuration.baseUrl + '/auth/login';
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#httpClient.get<ResponsePayload>(url, { headers }).pipe(
            map((rp) => rp.message)
        );
    }

    public createSession(authResult: AuthResult): Observable<UserSession> {

        const url = this.#configuration.baseUrl + '/auth/session';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        const obs$: Observable<ResponsePayload> = this.#httpClient.post<ResponsePayload>(url, authResult.idToken, {headers});
        return mapResponseDataToType<UserSession>(obs$, anonymousSession);

    }


    public logOut(sessionId: string | undefined): Observable<ResponsePayload> {

        const url = sessionId === undefined ? this.#configuration.baseUrl + '/auth/logout' : this.#configuration.baseUrl + '/auth/dev/logout/' + sessionId;
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#httpClient.delete<ResponsePayload>(url, {headers});


    }

}
