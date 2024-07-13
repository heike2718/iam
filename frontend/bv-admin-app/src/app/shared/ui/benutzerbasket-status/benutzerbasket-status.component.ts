import { CommonModule, NgIf } from "@angular/common";
import { Component, inject } from "@angular/core";
import { BenutzerFacade } from "@bv-admin-app/benutzer/api";
import { AuthFacade } from "@bv-admin-app/shared/auth/api";


@Component({
    selector: 'bv-benutzerbasket-status',
    standalone: true,
    imports: [
        CommonModule,
        NgIf
    ],
    templateUrl: './benutzerbasket-status.component.html',
    styleUrls: ['./benutzerbasket-status.component.scss'],
})
export class BenutzerBasketStatusComponend {


    authFacade = inject(AuthFacade);
    benutzerFacade = inject(BenutzerFacade);

}