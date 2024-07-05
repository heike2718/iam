import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { Component, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatTableModule } from "@angular/material/table";
import { BenutzerFacade } from "@bv-admin-app/benutzer/api";


@Component({
    selector: 'bv-users',
    standalone: true,
    imports: [
      CommonModule,
      NgIf,
      NgFor,
      AsyncPipe,
      MatTableModule,
      MatButtonModule
    ],
    templateUrl: './benutzer-basket.component.html',
    styleUrls: ['./benutzer-basket.component.scss'],
  })
export class BenutzerBasketComponent {

    benutzerFacade = inject(BenutzerFacade);

    reset(): void {
        this.benutzerFacade.resetBenutzerBasket();
    }

}