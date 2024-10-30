import { Component, inject, OnInit } from '@angular/core';
import { ProfilAppConfiguration } from './configuration/profil-app.configuration';
import { ShellComponent } from './shell/shell.component';
import { AuthFacade } from '@profil-app/auth/api';

@Component({
  standalone: true,
  imports: [
     ShellComponent
  ],
  selector: 'profil-app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {

  configuration = inject(ProfilAppConfiguration);
  imageSourceLogo = this.configuration.assetsPath + 'mja_logo_2.svg';


  #authService = inject(AuthFacade)

  ngOnInit(): void {
    this.#authService.initClearOrRestoreSession();
  }
}
