import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { Component, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { ConfirmationDialogComponent } from "@bv-admin-app/shared/ui/components";
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

    canAktualisieren(versandauftrag: MailversandauftragDetails): boolean {
        if (!versandauftrag) {
            return false;
        }
        return versandauftrag.status === 'IN_PROGRESS' || versandauftrag.status === 'WAITING';
    }


    btnRefreshDisabled(versandauftrag: MailversandauftragDetails): boolean {

        if (!versandauftrag) {
            return true;
        }

        return versandauftrag.status === 'COMPLETED' || versandauftrag.status === 'ERRORS';
    }

    canFortsetzen(versandauftrag: MailversandauftragDetails): boolean {
        if (!versandauftrag) {
            return false;
        }
        return versandauftrag.status === 'CANCELLED';
    }

    canAbbrechen(versandauftrag: MailversandauftragDetails): boolean {
        if (!versandauftrag) {
            return false;
        }
        return versandauftrag.status === 'IN_PROGRESS' || versandauftrag.status === 'WAITING';
    }

    showDetails(gruppe: Mailversandgruppe): void {
        this.versandauftraegeFacade.loadMailversandgruppe(gruppe.uuid);
    }

    goBack(): void {
        this.versandauftraegeFacade.unselectVersandauftrag();
    }

    continue(versandauftrag: MailversandauftragDetails): void {
        this.versandauftraegeFacade.continueVersandauftrag(versandauftrag.uuid);
    }

    cancel(versandauftrag: MailversandauftragDetails): void {

        const dialogRef = this.confirmDeleteDialog.open(ConfirmationDialogComponent, {
            width: '400px',
            disableClose: true,
            data: {
                title: 'Mailversandauftrag abbrechen',
                question: 'Bist Du vollkommen sicher, dass Du den Mailversandauftrag "' + versandauftrag.betreff + '" von ' + versandauftrag.erfasstAm + ' abbrechen willst?'
            }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.#doCancel(versandauftrag);
            }
        });
    }

    #doCancel(versandauftrag: MailversandauftragDetails): void {
        this.versandauftraegeFacade.cancelVersandauftrag(versandauftrag.uuid);

    }


}
