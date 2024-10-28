import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { AuthFacade } from "@profil-app/auth/api";


@Component({
  selector: 'profil-benutzerdaten',
  templateUrl: './benutzerdaten.component.html',
  styleUrl: './benutzerdaten.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    AsyncPipe
  ]
})
export class BenutzerdatenComponent {

  authFacade = inject(AuthFacade);
}