import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject, OnInit } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { AuthFacade } from "@profil-app/auth/api";
import { ProfilAppConfiguration } from "../configuration/profil-app.configuration";
import { Router } from "@angular/router";
import { ShellService } from "../shell/shell.service";


@Component({
    selector: 'profil-home',
    templateUrl: './home.component.html',
    styleUrl: './home.component.scss',
    standalone: true,
    imports: [
      CommonModule,
      MatButtonModule,
      MatIconModule,
      AsyncPipe
    ]
  })
  export class HomeComponent implements OnInit {

    imageSourceLogo = '';
    authFacade = inject(AuthFacade);
    shellService = inject(ShellService);

    #config = inject(ProfilAppConfiguration);
    #router = inject(Router);

    ngOnInit(): void {
        this.imageSourceLogo = this.#config.assetsPath + '/mja_logo_2.svg';
    }   

    gotoBenutzerdaten() {
        this.#router.navigateByUrl('benutzerdaten');
    }

    gotoPasswort() {
        this.#router.navigateByUrl('passwort');
    }

    gotoLoeschung() {
        this.#router.navigateByUrl('loeschung-benutzerkonto');
    }

    login() {
        this.authFacade.login();
    }

    logout() {
        this.authFacade.logout();
    }
    
}