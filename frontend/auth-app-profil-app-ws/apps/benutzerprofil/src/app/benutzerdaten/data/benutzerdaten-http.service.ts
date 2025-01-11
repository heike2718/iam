import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Benutzerdaten, ChangeBenutzerdatenResponseDto } from "../model";
import { Message } from "@ap-ws/common-model";


@Injectable({
    providedIn: 'root'
})
export class BenutzerdatenHttpService {

    #httpClient = inject(HttpClient);
    #url = '/benutzerprofil/api/benutzer';

    public loadBenutzerdaten(): Observable<Benutzerdaten> {

        return this.#httpClient.get<Benutzerdaten>(this.#url);
        
    }

    public updateBenutzerdaten(benutzerdaten: Benutzerdaten): Observable<ChangeBenutzerdatenResponseDto> {

        return this.#httpClient.put<ChangeBenutzerdatenResponseDto>(this.#url, benutzerdaten);

    }

    public deleteBenutzer(): Observable<Message> {

        return this.#httpClient.delete<Message>(this.#url);
    }

}