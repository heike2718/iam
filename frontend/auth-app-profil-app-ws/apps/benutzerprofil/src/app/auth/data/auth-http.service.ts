import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
// eslint-disable-next-line @nx/enforce-module-boundaries
import { AuthResult, Session } from "@benutzerprofil/auth/model";
import { Message } from "@ap-ws/common-model";


@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #httpClient = inject(HttpClient);
    #url = '/api/session';


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
