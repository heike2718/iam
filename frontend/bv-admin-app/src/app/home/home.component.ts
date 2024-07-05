import { AsyncPipe, CommonModule, NgIf } from "@angular/common";
import { Component, OnInit, inject } from "@angular/core";
import { AuthFacade } from "../shared/auth/api/auth.facade";
import { BenutzerFacade } from "@bv-admin-app/benutzer/api";


@Component({
    selector: 'bv-home',
    standalone: true,
    imports: [
        CommonModule,
        NgIf,
        AsyncPipe
    ],
    templateUrl: './home.component.html'
  })
export class HomeComponent {

    authFacade = inject(AuthFacade);
    benutzerFacade = inject(BenutzerFacade);

}