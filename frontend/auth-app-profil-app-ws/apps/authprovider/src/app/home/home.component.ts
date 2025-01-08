import { Component, inject } from "@angular/core";
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { AuthproviderConfiguration } from "@authprovider/configuration";
import { Router } from "@angular/router";


@Component({
    selector: 'authprovider-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        MatButtonModule
    ]
})
export class HomeComponent {

    configuration = inject(AuthproviderConfiguration);
    #router = inject(Router);

    redirectToProfilApp() {

		window.location.href = this.configuration.profilUrl;
	}
}