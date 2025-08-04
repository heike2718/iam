import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { ConfirmationDialogComponent } from "@bv-admin/shared/ui/components";
import { VersandauftraegeFacade } from "@bv-admin/versandauftraege/api";
import { MailversandauftragDetails, Mailversandgruppe } from "@bv-admin/versandauftraege/model";

@Component({
    selector: 'bv-admin-versandauftrag',
    imports: [
    CommonModule,
    AsyncPipe,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule
],
    templateUrl: './versandauftrag-details.component.html',
    styleUrls: ['./versandauftrag-details.component.scss']
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
        return versandauftrag.status !== 'COMPLETED';
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
        return versandauftrag.status === 'CANCELLED' || versandauftrag.status === 'ERRORS';
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
