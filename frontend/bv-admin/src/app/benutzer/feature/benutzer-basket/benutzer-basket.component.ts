import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { Component, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { BenutzerFacade } from "@bv-admin/benutzer/api";
import { Benutzer } from "@bv-admin/shared/model";

const UUID = 'uuid';
const EMAIL = 'email';
const NACHNAME = 'nachname';
const VORNAME = 'vorname';
const AENDERUNGSDATUM = 'aenderungsdatum';
const ROLLE = 'rolle';
const REMOVE_BENUTZER_ACTION = "remove"


@Component({
    selector: 'bv-users',
    standalone: true,
    imports: [
        CommonModule,
        NgIf,
        NgFor,
        AsyncPipe,
        MatTableModule,
        MatButtonModule,
        MatIconModule
    ],
    templateUrl: './benutzer-basket.component.html',
    styleUrls: ['./benutzer-basket.component.scss'],
})
export class BenutzerBasketComponent {

    benutzerFacade = inject(BenutzerFacade);

    reset(): void {
        this.benutzerFacade.resetBenutzerBasket();
    }

    getDisplayedColumns(): string[] {
        return [UUID, EMAIL, NACHNAME, VORNAME, AENDERUNGSDATUM, ROLLE, REMOVE_BENUTZER_ACTION];
    }

    removeBenutzerFromBasket(benutzer: Benutzer): void {
        this.benutzerFacade.removeBenutzerFromBasket(benutzer);
    }

}