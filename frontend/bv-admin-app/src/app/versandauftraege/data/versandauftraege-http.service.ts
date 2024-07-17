import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { MailversandauftragRequestDto } from "@bv-admin-app/shared/model";
import { Observable } from "rxjs";
import { MailversandauftragDetailsResponseDto, MailversandauftragOverview } from '@bv-admin-app/versandauftraege/model';

@Injectable({
    providedIn: 'root'
})
export class VersandauftraegeHttpService {

    #url = '/auth-admin-api/mailversand/auftraege';
    #httpClient = inject(HttpClient);

    public loadVersandauftraege(): Observable<MailversandauftragOverview[]> {
        return this.#httpClient.get<MailversandauftragOverview[]>(this.#url, { headers: new HttpHeaders() });
    }

    public loadDetails(uuid: string): Observable<MailversandauftragDetailsResponseDto> {
        const theUrl = this.#url + '/' + uuid;
        return this.#httpClient.get<MailversandauftragDetailsResponseDto>(theUrl, { headers: new HttpHeaders() });
    }


    public scheduleVersandauftrag(requestDto: MailversandauftragRequestDto): Observable<MailversandauftragOverview> {

        return this.#httpClient.post<MailversandauftragOverview>(this.#url, requestDto, { headers: new HttpHeaders() });
    }

}