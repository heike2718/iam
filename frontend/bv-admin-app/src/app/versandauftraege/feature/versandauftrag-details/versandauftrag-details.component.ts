import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { AfterViewInit, Component, inject, OnDestroy } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { VersandauftraegeFacade } from "@bv-admin-app/versandauftraege/api";
import { MailversandauftragDetails } from "@bv-admin-app/versandauftraege/model";
import { Subscription } from "rxjs";

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
export class VersandauftragDetailsComponent implements AfterViewInit, OnDestroy {


    versandauftraegeFacade = inject(VersandauftraegeFacade);

    #subscriptions: Subscription = new Subscription();

    constructor(public confirmDeleteDialog: MatDialog) {
    }

    ngAfterViewInit(): void {

        // Error: NG0100: ExpressionChangedAfterItHasBeenCheckedError: Expression has changed after it was checked.
        // haben wir immernoch :()
    }

    ngOnDestroy(): void {
        this.#subscriptions.unsubscribe();
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
}
