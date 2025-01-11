import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Message, PasswortPayload } from "@ap-ws/common-model";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class PasswortHttpService {


    #url = '/benutzerprofil/api/passwort';
    #httpClient = inject(HttpClient);




    public changePasswort(passwortPayload: PasswortPayload): Observable<Message> {

        return this.#httpClient.put<Message>(this.#url, passwortPayload);

    }

}