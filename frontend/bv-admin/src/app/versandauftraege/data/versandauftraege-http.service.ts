import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { MailversandauftragRequestDto, SingleUuidDto } from "@bv-admin/shared/model";
import { Observable } from "rxjs";
import { MailversandauftragDetailsResponseDto, MailversandauftragOverview, MailversandgruppeDetails, MailversandgruppeDetailsResponseDto } from '@bv-admin/versandauftraege/model';

@Injectable({
    providedIn: 'root'
})
export class VersandauftraegeHttpService {

    #url = '/bv-admin/api/versandauftraege';
    #httpClient = inject(HttpClient);

    public loadVersandauftraege(): Observable<MailversandauftragOverview[]> {
        return this.#httpClient.get<MailversandauftragOverview[]>(this.#url, { headers: new HttpHeaders() });
    }

    public loadVersandauftragDetails(uuid: string): Observable<MailversandauftragDetailsResponseDto> {
        const theUrl = this.#url + '/' + uuid;
        return this.#httpClient.get<MailversandauftragDetailsResponseDto>(theUrl, { headers: new HttpHeaders() });
    }

    public loadMailversandgruppedetails(uuid: string): Observable<MailversandgruppeDetailsResponseDto> {
        const theUrl = this.#url + '/gruppen/' + uuid;
        return this.#httpClient.get<MailversandgruppeDetailsResponseDto>(theUrl, { headers: new HttpHeaders() });
    }

    public scheduleVersandauftrag(requestDto: MailversandauftragRequestDto): Observable<MailversandauftragOverview> {
        return this.#httpClient.post<MailversandauftragOverview>(this.#url, requestDto, { headers: new HttpHeaders() });
    }

    public cancelVersandauftrag(uuid: string): Observable<SingleUuidDto> {
        const theUrl = this.#url + '/' + uuid + '/cancellation';
        return this.#httpClient.put<SingleUuidDto>(theUrl, { headers: new HttpHeaders() });
    }

    public continueVersandauftrag(uuid: string): Observable<SingleUuidDto> {
        const theUrl = this.#url + '/' + uuid + '/continuation';
        return this.#httpClient.put<SingleUuidDto>(theUrl, { headers: new HttpHeaders() });
    }

    public deleteVersandauftrag(uuid: string): Observable<SingleUuidDto> {
        const theUrl = this.#url + '/' + uuid;
        return this.#httpClient.delete<SingleUuidDto>(theUrl, { headers: new HttpHeaders() });
    }

    public updateMailversandgruppe(mailversandgruppe: MailversandgruppeDetails): Observable<MailversandgruppeDetailsResponseDto> {
        const theUrl = this.#url + '/gruppen/' + mailversandgruppe.uuid;
        return this.#httpClient.put<MailversandgruppeDetailsResponseDto>(theUrl, mailversandgruppe, { headers: new HttpHeaders() });
    }
}