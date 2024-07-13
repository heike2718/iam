import { CommonModule, NgFor } from "@angular/common";
import { Component, inject, Input } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import {MatCardModule} from '@angular/material/card';
import { BenutzerFacade } from "@bv-admin-app/benutzer/api";
import { InfomailsFacade } from "@bv-admin-app/infomails/api";
import { Infomail } from "@bv-admin-app/infomails/model";

@Component({
    selector: 'bv-infomail',
    standalone: true,
    imports: [
        CommonModule,
        NgFor,
        MatButtonModule,
        MatCardModule
    ],
    templateUrl: './infomails-details.component.html',
    styleUrls: ['./infomails-details.component.scss'],})
export class InfomailsDetailsComponent {

    benutzerFacade = inject(BenutzerFacade);

    @Input()
    infomail: Infomail | undefined;

    infomailsFacade = inject(InfomailsFacade);

    startEdit(): void {
        this.infomailsFacade.startEdit();
    }

    mailauftragErstellen(): void {

    }


}