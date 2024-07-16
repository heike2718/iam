import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { MailversandauftragOverview } from '@bv-admin-app/versandauftraege/model';
import { Store } from "@ngrx/store";
import { fromVersandauftraege, versandauftraegeActions } from '@bv-admin-app/versandauftraege/data';
import { MailversandauftragRequestDto } from "@bv-admin-app/shared/model";
import { Infomail } from "@bv-admin-app/infomails/model";

@Injectable(
    { providedIn: 'root' }
)
export class VersandauftraegeFacade {

    #store = inject(Store);

    readonly versandauftraege$: Observable<MailversandauftragOverview[]> = this.#store.select(fromVersandauftraege.versandauftraege);


    public scheduleMailversandauftrag(infomail: Infomail, uuids: string[]): void {


        const requestDto: MailversandauftragRequestDto = {
            benutzerUUIDs: uuids,
            idInfomailtext: infomail.uuid
        };

        this.#store.dispatch(versandauftraegeActions.sCHEDULE_VERSANDAUFTRAG({ requestDto }))

    }

}