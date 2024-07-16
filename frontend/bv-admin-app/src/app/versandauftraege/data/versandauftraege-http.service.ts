import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { MailversandauftragRequestDto } from "@bv-admin-app/shared/model";
import { Observable } from "rxjs";
import { MailversandauftragOverview } from '@bv-admin-app/versandauftraege/model';

@Injectable({
    providedIn: 'root'
})
export class VersandauftraegeHttpService {

    #url = '/auth-admin-api/mailversand/auftraege';
    #httpClient = inject(HttpClient);  

    scheduleVersandauftrag(requestDto: MailversandauftragRequestDto): Observable<MailversandauftragOverview> {
     
        return this.#httpClient.post<MailversandauftragOverview>(this.#url, requestDto, { headers: new HttpHeaders() });
    }

}