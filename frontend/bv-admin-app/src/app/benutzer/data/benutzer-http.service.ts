import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { BenutzerSearchResult, BenutzerSuchparameter, DeleteBenutzerResponseDto } from '@bv-admin-app/benutzer/model';
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

    deleteBenutzer(uuid: string): Observable<DeleteBenutzerResponseDto> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#httpClient.delete<DeleteBenutzerResponseDto>(this.#url + '/' + uuid, {headers});
    }

}