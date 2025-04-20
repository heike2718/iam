import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { AfterViewInit, ChangeDetectorRef, Component, inject, OnDestroy } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { ConfirmationDialogComponent } from "@bv-admin/shared/ui/components";
import { VersandauftraegeFacade } from "@bv-admin/versandauftraege/api";
import { MailversandauftragOverview } from "@bv-admin/versandauftraege/model";
import { Subscription } from "rxjs";


const BETREFF = 'betreff';
const ERFASST_AM = 'erfasstAm';
const STATUS = 'status';
const ANZAHL_EMPFAENGER = 'anzahlEmpfaenger';
const ANZAHL_GRUPPEN = 'anzahlGruppen';
const DETAILS_ACTION = 'details';
const CANCEL_ACTION = 'cancel';
const DELETE_ACTION = 'delete'

@Component({
    selector: 'bv-admin-versandauftraege',
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
    templateUrl: './versandauftraege-list.component.html',
    styleUrls: ['./versandauftraege-list.component.scss']
})
export class VersandauftraegeListComponent implements AfterViewInit, OnDestroy {

  versandauftraegeFacade = inject(VersandauftraegeFacade);

  #subscriptions: Subscription = new Subscription();

  constructor(private changeDetector: ChangeDetectorRef, public confirmDeleteDialog: MatDialog) {
  }

  ngAfterViewInit(): void {

    const loadedSubscription = this.versandauftraegeFacade.loaded$.subscribe(
      (loaded) => {
        if (!loaded) {
          this.versandauftraegeFacade.loadVersandauftraege();
        }
      }
    );

    this.#subscriptions.add(loadedSubscription);

  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
  }

  getDisplayedColumns(): string[] {
    return [BETREFF, ERFASST_AM, STATUS, ANZAHL_EMPFAENGER, ANZAHL_GRUPPEN, DETAILS_ACTION, CANCEL_ACTION, DELETE_ACTION];
  }

  selectVersandauftrag(row: MailversandauftragOverview): void {
     this.versandauftraegeFacade.loadVersandauftragDetails(row.uuid);
  }

  delete(versandauftrag: MailversandauftragOverview): void {

    const dialogRef = this.confirmDeleteDialog.open(ConfirmationDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Mailversandauftrag löschen',
        question: 'Bist Du vollkommen sicher, dass Du den Mailversandauftrag zu "' + versandauftrag.betreff + '" von ' + versandauftrag.erfasstAm + ' löschen willst?'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.#doDelete(versandauftrag);
      }
    });
  }

  cancel(versandauftrag: MailversandauftragOverview): void {

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


  #doDelete(versandauftrag: MailversandauftragOverview): void {
    this.versandauftraegeFacade.deleteVersandauftrag(versandauftrag);
  }

  #doCancel(versandauftrag: MailversandauftragOverview): void {
    this.versandauftraegeFacade.cancelVersandauftrag(versandauftrag.uuid);

  }

}