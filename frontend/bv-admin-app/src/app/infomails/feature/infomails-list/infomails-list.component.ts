import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { AfterViewInit, Component, inject, OnDestroy } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { InfomailsFacade } from '@bv-admin-app/infomails/api';
import { Infomail } from "@bv-admin-app/infomails/model";
import { combineLatest, Subscription } from "rxjs";
import { InfomailsDetailsComponent } from "../infomails-details/infomails-details.component";
import { BenutzerFacade } from "@bv-admin-app/benutzer/api";
import { Benutzer } from "@bv-admin-app/benutzer/model";

const UUID = 'uuid';
const BETREFF = 'betreff';
const ANZAHL_VERSENDET = "anzahlVersendet"

@Component({
  selector: 'bv-infomails',
  standalone: true,
  imports: [
    CommonModule,
    InfomailsDetailsComponent,
    NgIf,
    NgFor,
    AsyncPipe,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './infomails-list.component.html',
  styleUrls: ['./infomails-list.component.scss'],
})
export class InfomailsListComponent implements OnDestroy, AfterViewInit {

  infomailsFacade = inject(InfomailsFacade);
  benutzerFacade = inject(BenutzerFacade);

  selectedInfomail: Infomail | undefined;
  benutzerBasket: Benutzer[] = [];

  // eine für alle mit add :)
  #subscriptions: Subscription = new Subscription();

  ngAfterViewInit(): void {

    const loadedSubscription = this.infomailsFacade.infomailsLoaded$.subscribe((loaded) => {
      if (!loaded) {
        this.infomailsFacade.loadInfomails();
      }
    });

    const combinedStatusSubscription = combineLatest([this.infomailsFacade.selectedInfomail$,
      this.benutzerFacade.benutzerBasket$]).subscribe(
        ([infomailText, benutzerBasket]) => {
          this.selectedInfomail = infomailText;
          this.benutzerBasket = benutzerBasket
        }
      );

    this.#subscriptions.add(loadedSubscription);
    this.#subscriptions.add(combinedStatusSubscription);

  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
  }


  getDisplayedColumns(): string[] {
    return [UUID, BETREFF,ANZAHL_VERSENDET];
  }

  selectInfomail(infomail: Infomail) {
    this.infomailsFacade.selectInfomail(infomail);
  }

  buttonMailauftragErstellenDisabled(): boolean {
    if (!this.selectedInfomail) {
      return true;
    }
    return this.benutzerBasket.length === 0;
  }

  mailauftragErstellen(): void {

  }

}