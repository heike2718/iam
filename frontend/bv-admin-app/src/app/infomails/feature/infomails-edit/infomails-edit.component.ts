import { CommonModule } from "@angular/common";
import { Component, inject, Input } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { InfomailsFacade } from "@bv-admin-app/infomails/api";
import { Infomail } from "@bv-admin-app/infomails/model";


@Component({
    selector: 'bv-infomail-edit',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatButtonModule,
        MatCardModule
    ],
    templateUrl: './infomails-edit.component.html',
    styleUrls: ['./infomails-edit.component.scss'],
})
export class InfomailEditComponent {

    @Input()
    infomail!: Infomail;

    infomailsFacade = inject(InfomailsFacade);

    save(): void {
        this.infomailsFacade.saveInfomailText(this.infomail.uuid, { betreff: this.infomail.betreff, mailtext: this.infomail.mailtext });
    }

    cancelEdit(): void {
        this.infomailsFacade.cancelEdit();
    }
}