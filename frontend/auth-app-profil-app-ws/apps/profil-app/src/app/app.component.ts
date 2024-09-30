import { Component, inject } from '@angular/core';
import { ProfilAppConfiguration } from './configuration/profil-app.configuration';
import { ShellComponent } from './shell/shell.component';
import { ShellService } from './shell/shell.service';

@Component({
  standalone: true,
  imports: [
     ShellComponent
  ],
  selector: 'profil-app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {

  configuration = inject(ProfilAppConfiguration);
  imageSourceLogo = this.configuration.assetsPath + 'mja_logo_2.svg';
}
