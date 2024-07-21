import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { Component, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatTableModule } from "@angular/material/table";
import { VersandauftraegeFacade } from "@bv-admin-app/versandauftraege/api";
import { MailversandgruppeDetails } from "@bv-admin-app/versandauftraege/model";


const UUID = 'uuid';
const EMAIL = 'email';
const NACHNAME = 'nachname';
const VORNAME = 'vorname';
const AENDERUNGSDATUM = 'aenderungsdatum';
const ROLLE = 'rolle';

@Component({
    selector: 'bv-admin-versandgruppe',
    standalone: true,
    imports: [
        CommonModule,
        NgIf,
        NgFor,
        AsyncPipe,
        MatButtonModule,
        MatTableModule
    ],
    templateUrl: './versandgruppe-details.component.html',
    styleUrls: ['./versandgruppe-details.component.scss'],
})
export class VersandgruppeDetailsComponent {

    versandauftraegeFacade = inject(VersandauftraegeFacade);

    getDisplayedColumns(): string[] {
        return [UUID, EMAIL, NACHNAME, VORNAME, AENDERUNGSDATUM, ROLLE];
    }

    refresh(gruppe: MailversandgruppeDetails): void {

        if (!gruppe) {
            return;
        }

        this.versandauftraegeFacade.loadMailversandgruppe(gruppe.uuid);

    }

    btnRefreshDisabled(gruppe: MailversandgruppeDetails): boolean {

        if (!gruppe) {
            return true;
        }

        return gruppe.status === 'COMPLETED' || gruppe.status === 'ERRORS';
    }

}