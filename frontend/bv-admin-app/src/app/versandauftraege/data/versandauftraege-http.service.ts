import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { MailversandauftragRequestDto } from "@bv-admin-app/shared/model";
import { Observable } from "rxjs";
import { MailversandauftragDetailsResponseDto, MailversandauftragOverview, MailversandgruppeDetailsResponseDto } from '@bv-admin-app/versandauftraege/model';

@Injectable({
    providedIn: 'root'
})
export class VersandauftraegeHttpService {

    #url = '/auth-admin-api/mailversand';
    #httpClient = inject(HttpClient);

    public loadVersandauftraege(): Observable<MailversandauftragOverview[]> {
        const theUrl = this.#url + '/auftraege';
        return this.#httpClient.get<MailversandauftragOverview[]>(theUrl, { headers: new HttpHeaders() });
    }

    public loadVersandauftragDetails(uuid: string): Observable<MailversandauftragDetailsResponseDto> {
        const theUrl = this.#url + '/auftraege/' + uuid;
        return this.#httpClient.get<MailversandauftragDetailsResponseDto>(theUrl, { headers: new HttpHeaders() });
    }

    public loadMailversandgruppeetails(uuid: string): Observable<MailversandgruppeDetailsResponseDto> {
        const theUrl = this.#url + '/gruppen/' + uuid;
        return this.#httpClient.get<MailversandgruppeDetailsResponseDto>(theUrl, { headers: new HttpHeaders() });
    }


    public scheduleVersandauftrag(requestDto: MailversandauftragRequestDto): Observable<MailversandauftragOverview> {
        const theUrl = this.#url + '/auftraege';
        return this.#httpClient.post<MailversandauftragOverview>(theUrl, requestDto, { headers: new HttpHeaders() });
    }
}