import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { Message } from "@bv-admin/shared/messages/api";
import { AuthResult, Session } from "@bv-admin/shared/auth/model";



@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #url = '/bv-admin/api';
    #httpClient = inject(HttpClient);

    getLoginUrl(): Observable<Message> {
        return this.#httpClient.get<Message>(this.#url + '/session/authurls/login');
    }

    createSession(authResult: AuthResult): Observable<Session> {

        return this.#httpClient.post<Session>(this.#url +  '/session/login', authResult);
    }

    logOut(): Observable<Message> {

        return this.#httpClient.delete<Message>(this.#url +  '/session/logout');
    }

}