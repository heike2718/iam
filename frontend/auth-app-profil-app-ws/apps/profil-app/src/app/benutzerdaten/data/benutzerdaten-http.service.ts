import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Benutzerdaten } from "../model";


@Injectable({
    providedIn: 'root'
})
export class BenutzerdatenHttpService {

    #httpClient = inject(HttpClient);
    #url = '/profil-api/benutzer';

    public loadBenutzerdaten(): Observable<Benutzerdaten> {

        return this.#httpClient.get<Benutzerdaten>(this.#url);
        
    }

    public updateBenutzerdaten(benutzerdaten: Benutzerdaten): Observable<Benutzerdaten> {

        return this.#httpClient.put<Benutzerdaten>(this.#url, benutzerdaten);

    }

}