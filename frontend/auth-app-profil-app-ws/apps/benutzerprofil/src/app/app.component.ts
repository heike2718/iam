import { Component, inject, OnInit } from '@angular/core';
import { BenutzerprofilConfiguration } from '@benutzerprofil/configuration';
import { ShellComponent } from './shell/shell.component';
import { AuthFacade } from '@benutzerprofil/auth/api';

@Component({
  standalone: true,
  imports: [
     ShellComponent
  ],
  selector: 'benutzerprofil-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {

  configuration = inject(BenutzerprofilConfiguration);
  imageSourceLogo = this.configuration.assetsPath + '/mja_logo_2.svg';


  #authService = inject(AuthFacade)

  ngOnInit(): void {
    this.#authService.initClearOrRestoreSession();
  }
}
