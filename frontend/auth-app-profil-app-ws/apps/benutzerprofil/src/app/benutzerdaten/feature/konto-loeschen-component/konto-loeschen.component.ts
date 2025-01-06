import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { Router } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { FormsModule } from "@angular/forms";
import { BenutzerdatenFacade } from "@benutzerprofil/benutzerdaten/api";


@Component({
  selector: 'benutzerprofil-konto-loeschen',
  templateUrl: './konto-loeschen.component.html',
  styleUrl: './konto-loeschen.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    FormsModule,
    AsyncPipe
  ]
})
export class KontoLoeschenComponent {

  benutzerdatenFacade = inject(BenutzerdatenFacade);
  confirmed = false;

  #router = inject(Router);

  deleteAccount() {
    this.benutzerdatenFacade.benutzerkontoLoeschen();
  }

  gotoStartseite() {
    this.#router.navigateByUrl('/home');
  }

}