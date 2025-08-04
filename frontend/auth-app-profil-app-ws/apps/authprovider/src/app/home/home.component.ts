import { Component, inject } from "@angular/core";

import { MatButtonModule } from '@angular/material/button';
import { AuthproviderConfiguration } from "@authprovider/configuration";


@Component({
    selector: 'authprovider-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
    imports: [
    MatButtonModule
]
})
export class HomeComponent {

    configuration = inject(AuthproviderConfiguration);

    redirectToProfilApp() {

		window.location.href = this.configuration.profilUrl;
	}
}