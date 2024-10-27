import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { AuthFacade } from "@profil-app/auth/api";


@Component({
    selector: 'profil-konto-loeschen',
    templateUrl: './konto-loeschen.component.html',
    styleUrl: './konto-loeschen.component.scss',
    standalone: true,
    imports: [
      CommonModule,
      AsyncPipe
    ]
  })
export class KontoLoeschenComponent {

    authFacade = inject(AuthFacade);

}