import { CommonModule } from "@angular/common";
import { Component, inject, Input, OnDestroy, OnInit } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from '@angular/material/card';
import { BenutzerFacade } from "@bv-admin/benutzer/api";
import { InfomailsFacade } from "@bv-admin/infomails/api";
import { Infomail } from "@bv-admin/shared/model";
import { VersandauftraegeFacade } from "@bv-admin/versandauftraege/api";
import { Subscription } from "rxjs";

@Component({
    selector: 'bv-admin-infomail',
    imports: [
        CommonModule,
        MatButtonModule,
        MatCardModule
    ],
    templateUrl: './infomails-details.component.html',
    styleUrls: ['./infomails-details.component.scss']
})
export class InfomailsDetailsComponent implements OnInit, OnDestroy {

    @Input()
    infomail: Infomail | undefined;

    infomailsFacade = inject(InfomailsFacade);

    #benutzerFacade = inject(BenutzerFacade);
    #versandauftraegeFacade = inject(VersandauftraegeFacade);

    #uuidsGewaehlteBenutzer: string[] = [];
    #subscriptions: Subscription = new Subscription();

    ngOnInit(): void {

        const benutzerBasketSubscription = this.#benutzerFacade.uuidsGewaehlteBenutzer$.subscribe(
            (uuids) => this.#uuidsGewaehlteBenutzer = uuids
        );

        this.#subscriptions.add(benutzerBasketSubscription);

    }

    ngOnDestroy(): void {
        this.#subscriptions.unsubscribe();
    }

    startEdit(): void {
        this.infomailsFacade.startEdit();
    }

    mailauftragErstellen(): void {

        if (this.infomail && this.#uuidsGewaehlteBenutzer.length > 0) {
            this.#versandauftraegeFacade.scheduleMailversandauftrag(this.infomail, this.#uuidsGewaehlteBenutzer)
        }
    }

    btnMailauftragErstellenDisabled(): boolean {

        return this.infomail === undefined || this.#uuidsGewaehlteBenutzer.length === 0;

    }


}