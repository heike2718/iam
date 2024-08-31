import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { Component, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { VersandauftraegeFacade } from "@bv-admin-app/versandauftraege/api";
import { MailversandauftragDetails, Mailversandgruppe } from "@bv-admin-app/versandauftraege/model";

@Component({
    selector: 'bv-admin-versandauftrag',
    standalone: true,
    imports: [
        CommonModule,
        NgIf,
        NgFor,
        AsyncPipe,
        MatTableModule,
        MatButtonModule,
        MatIconModule,
        MatDialogModule
    ],
    templateUrl: './versandauftrag-details.component.html',
    styleUrls: ['./versandauftrag-details.component.scss'],
})
export class VersandauftragDetailsComponent {


    versandauftraegeFacade = inject(VersandauftraegeFacade);

    versandgruppenDisplayedColumns: string[] = ['sortnr', 'status', 'aenderungsdatum', 'anzahlEmpfaenger', 'detailsAction'];

    constructor(public confirmDeleteDialog: MatDialog) {
    }


    refresh(versandauftrag: MailversandauftragDetails): void {

        if (!versandauftrag) {
            return;
        }

        this.versandauftraegeFacade.refreshVersandauftrag(versandauftrag.uuid);

    }

    btnRefreshDisabled(versandauftrag: MailversandauftragDetails): boolean {

        if (!versandauftrag) {
            return true;
        }

        return versandauftrag.status === 'COMPLETED' || versandauftrag.status === 'ERRORS';
    }

    showDetails(gruppe: Mailversandgruppe): void {
        this.versandauftraegeFacade.loadMailversandgruppe(gruppe.uuid);
    }

    goBack(): void {
        this.versandauftraegeFacade.unselectVersandauftrag();
    }

    
}
