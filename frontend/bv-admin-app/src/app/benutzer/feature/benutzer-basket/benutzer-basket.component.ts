import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { Component, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatTableModule } from "@angular/material/table";
import { BenutzerFacade } from "@bv-admin-app/benutzer/api";

const UUID = 'uuid';
const EMAIL = 'email';
const NACHNAME = 'nachname';
const VORNAME = 'vorname';
const DATE_MODIFIED = 'dateModified';
const ROLLE = 'rolle';


@Component({
    selector: 'bv-users',
    standalone: true,
    imports: [
        CommonModule,
        NgIf,
        NgFor,
        AsyncPipe,
        MatTableModule,
        MatButtonModule
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
        return [UUID, EMAIL, NACHNAME, VORNAME, DATE_MODIFIED, ROLLE];
    }

}