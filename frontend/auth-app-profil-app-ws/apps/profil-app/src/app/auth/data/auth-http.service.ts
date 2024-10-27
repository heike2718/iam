import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { map, Observable } from "rxjs";
import { mapResponseDataToType } from '@ap-ws/common-utils';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { anonymousSession, AuthResult, Session } from "apps/profil-app/src/app/auth/model";
import { Message, ResponsePayload } from "@ap-ws/common-model";


@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #httpClient = inject(HttpClient);
    #url = '/profil-api/session';


    public getLoginUrl(): Observable<Message> {
        return this.#httpClient.get<Message>(this.#url + '/authurls/login');
    }

    public createSession(authResult: AuthResult): Observable<Session> {
        return this.#httpClient.post<Session>(this.#url +  '/login', authResult);
    }


    public logOut(): Observable<Message> {
        return this.#httpClient.delete<Message>(this.#url +  '/logout');
    }

}
