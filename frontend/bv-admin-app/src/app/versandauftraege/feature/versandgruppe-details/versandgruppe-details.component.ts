import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { Component } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";


@Component({
    selector: 'bv-admin-versandgruppe',
    standalone: true,
    imports: [
        CommonModule,
        NgIf,
        NgFor,
        AsyncPipe,
        MatButtonModule
    ],
    templateUrl: './versandgruppe-details.component.html',
    styleUrls: ['./versandgruppe-details.component.scss'],
})
export class VersandgruppeDetailsComponent {

}