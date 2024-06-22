import { AsyncPipe, CommonModule, NgIf } from "@angular/common";
import { Component, OnInit, inject } from "@angular/core";
import { AuthFacade } from "../shared/auth/api/auth.facade";


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

}