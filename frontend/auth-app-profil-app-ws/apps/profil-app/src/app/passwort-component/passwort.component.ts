import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { AuthFacade } from "@profil-app/auth/api";


@Component({
    selector: 'profil-passwort',
    templateUrl: './passwort.component.html',
    styleUrl: './passwort.component.scss',
    standalone: true,
    imports: [
      CommonModule,
      AsyncPipe
    ]
  })
  export class PasswortComponent {

    authFacade = inject(AuthFacade);

}