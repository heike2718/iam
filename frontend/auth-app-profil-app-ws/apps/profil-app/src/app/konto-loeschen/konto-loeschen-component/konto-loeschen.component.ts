import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { AuthFacade } from "@profil-app/auth/api";
import { Router } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";


@Component({
  selector: 'profil-konto-loeschen',
  templateUrl: './konto-loeschen.component.html',
  styleUrl: './konto-loeschen.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    AsyncPipe
  ]
})
export class KontoLoeschenComponent {

  authFacade = inject(AuthFacade);

  #router = inject(Router);

  gotoStartseite() {
    this.#router.navigateByUrl('/home');
  }

}