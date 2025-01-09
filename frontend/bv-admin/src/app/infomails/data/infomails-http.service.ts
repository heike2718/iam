import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { InfomailRequestDto, UpdateInfomailResponseDto } from "@bv-admin/infomails/model";
import { Infomail } from "@bv-admin/shared/model";

@Injectable({
    providedIn: 'root'
})
export class InfomailsHttpService {

    #url = '/api/infomails';
    #httpClient = inject(HttpClient);  

    public loadInfomailTexte(): Observable<Infomail[]> {

        return this.#httpClient.get<Infomail[]>(this.#url, { headers: new HttpHeaders() });

    }

    public insertInfomailtext(requestDto: InfomailRequestDto): Observable<Infomail> {

        return this.#httpClient.post<Infomail>(this.#url, requestDto, { headers: new HttpHeaders() });

    }

    public updateInfomail(uuid: string, requestDto: InfomailRequestDto): Observable<UpdateInfomailResponseDto> {
        
        const theUrl = this.#url + '/' + uuid;
        return this.#httpClient.put<UpdateInfomailResponseDto>(theUrl, requestDto, { headers: new HttpHeaders() });
    }

}