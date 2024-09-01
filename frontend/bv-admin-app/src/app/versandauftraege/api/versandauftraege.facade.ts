import { inject, Injectable } from "@angular/core";
import { Observable, Subscription, take } from "rxjs";
import { MailversandauftragDetails, MailversandauftragOverview, Mailversandgruppe, MailversandgruppeDetails } from '@bv-admin-app/versandauftraege/model';
import { select, Store } from "@ngrx/store";
import { fromVersandauftraege, versandauftraegeActions } from '@bv-admin-app/versandauftraege/data';
import { MailversandauftragRequestDto, Infomail, Benutzer } from "@bv-admin-app/shared/model";

@Injectable(
    { providedIn: 'root' }
)
export class VersandauftraegeFacade {

    #store = inject(Store);

    #versandauftraegeDetailsSubscription: Subscription = new Subscription();


    readonly loaded$: Observable<boolean> = this.#store.select(fromVersandauftraege.loaded);
    readonly versandauftraege$: Observable<MailversandauftragOverview[]> = this.#store.select(fromVersandauftraege.versandauftraege);
    readonly selectedVersandauftrag$: Observable<MailversandauftragDetails | undefined> =
        this.#store.select(fromVersandauftraege.selectedVersandauftrag);
    readonly selectedMailversandgruppe$: Observable<MailversandgruppeDetails | undefined> = 
    this.#store.select(fromVersandauftraege.selectedMailversandgruppe);

    public loadVersandauftraege(): void {
        this.#store.dispatch(versandauftraegeActions.lOAD_VERSANDAUFTRAEGE());
    }

    public loadVersandauftragDetails(uuid: string): void {

        this.#versandauftraegeDetailsSubscription.unsubscribe();

        this.#versandauftraegeDetailsSubscription = this.#store.pipe(
            select(fromVersandauftraege.versandauftraegeDetails),
            take(1)
        ).subscribe(
            (details: MailversandauftragDetails[]) => {
                const filtered = details.filter(d => d.uuid === uuid);
                if (filtered.length === 1 && (filtered[0].status === 'COMPLETED' || filtered[0].status === 'ERRORS')) {
                    this.#selectDetails(filtered[0]);
                } else {
                    this.#loadDetails(uuid);
                }
            }
        );
    }

    public refreshVersandauftrag(uuid: string): void {
        this.#loadDetails(uuid);
    }

    public scheduleMailversandauftrag(infomail: Infomail, uuids: string[]): void {

        const requestDto: MailversandauftragRequestDto = {
            benutzerUUIDs: uuids,
            idInfomailtext: infomail.uuid
        };

        this.#store.dispatch(versandauftraegeActions.sCHEDULE_VERSANDAUFTRAG({ requestDto }))

    }

    public cancelVersandauftrag(versandauftrag: MailversandauftragOverview): void {

        this.#store.dispatch(versandauftraegeActions.cANCEL_VERSANDAUFTRAG({uuid: versandauftrag.uuid}));
    }

    public continueVersandauftrag(versandauftrag: MailversandauftragDetails): void {

        this.#store.dispatch(versandauftraegeActions.cONTINUE_VERSANDAUFTRAG({uuid: versandauftrag.uuid}));
    }


    public deleteVersandauftrag(versandauftrag: MailversandauftragOverview): void {

        if (versandauftrag.status === 'IN_PROGRESS') {
            return;
        }

        this.#store.dispatch(versandauftraegeActions.dELETE_VERSANDAUFTRAG({uuid: versandauftrag.uuid}))
    }

    public loadMailversandgruppe(uuid: string): void {

        this.#store.dispatch(versandauftraegeActions.lOAD_VERSANDGRUPPE({uuid}));
    }   
    
    public updateMailversandgruppe(mailversandgruppe: MailversandgruppeDetails): void {

        this.#store.dispatch(versandauftraegeActions.vERSANDGRUPPE_SPEICHERN({mailversandgruppe: mailversandgruppe}));
    }

    public removeBenutzerFromMailversandgruppe(benutzer: Benutzer, mailversandgruppe: MailversandgruppeDetails): void {

        this.#store.dispatch(versandauftraegeActions.vERSANDGRUPPE_BENUTZER_ENTFERNEN({benutzer, mailversandgruppe}));
    }

    public unselectVersandauftrag(): void {
        this.#store.dispatch(versandauftraegeActions.uNSELECT_VERSANDAUFTRAG());
    }

    public unselectVersandgruppe(): void {
        this.#store.dispatch(versandauftraegeActions.uNSELECT_VERSANDGRUPPE());
    }

    #loadDetails(uuid: string): void {
        this.#store.dispatch(versandauftraegeActions.lOAD_VERSANDAUFTRAG_DETAILS({ uuid }));
    }

    #selectDetails(versandauftrag: MailversandauftragDetails): void {
        this.#store.dispatch(versandauftraegeActions.sELECT_VERSANDAUFTRAG({ versandauftrag }));
    }

}