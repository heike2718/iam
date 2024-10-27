import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { AuthFacade } from "@profil-app/auth/api";


@Component({
  selector: 'profil-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    AsyncPipe
  ]
})
export class HomeComponent {

  authFacade = inject(AuthFacade);
}