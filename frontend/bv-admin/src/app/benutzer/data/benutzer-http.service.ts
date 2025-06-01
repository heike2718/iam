import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Aktivierungsstatus, BenutzerSearchResult, BenutzerSuchparameter, DeleteBenutzerResponseDto, UpdateBenutzerResponseDto } from '@bv-admin/benutzer/model';
import { FlagsDto } from "@bv-admin/shared/model";
import { Observable } from "rxjs";



@Injectable({
    providedIn: 'root'
})
export class BenutzerHttpService {

    #url = '/bv-admin/api/benutzer';
    #httpClient = inject(HttpClient);   


    findBenutzer(suchparameter: BenutzerSuchparameter): Observable<BenutzerSearchResult> {

        return this.#httpClient.post<BenutzerSearchResult>(this.#url, suchparameter, { headers: new HttpHeaders() });

    }

    deleteBenutzer(uuid: string): Observable<DeleteBenutzerResponseDto> {

        return this.#httpClient.delete<DeleteBenutzerResponseDto>(this.#url + '/' + uuid, { headers: new HttpHeaders() });
    }

    updateBenutzerFlags(uuid: string, flagsDto: FlagsDto): Observable<UpdateBenutzerResponseDto> {

        return this.#httpClient.put<UpdateBenutzerResponseDto>(this.#url + '/' + uuid, flagsDto,   { headers: new HttpHeaders() });

    }

}