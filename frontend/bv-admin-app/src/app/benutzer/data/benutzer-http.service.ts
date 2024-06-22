import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { BenutzerSearchResult, BenutzerSuchparameter } from '@bv-admin-app/benutzer/model';
import { Observable } from "rxjs";



@Injectable({
    providedIn: 'root'
})
export class BenutzerHttpService {

    #url = '/auth-admin-api/benutzer';
    #httpClient = inject(HttpClient);   


    findBenutzer(suchparameter: BenutzerSuchparameter): Observable<BenutzerSearchResult> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#httpClient.post<BenutzerSearchResult>(this.#url, suchparameter, { headers });

    }

}