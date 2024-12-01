import { Component, inject } from "@angular/core";
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { AuthAppConfiguration } from "../config/auth-app.configuration";
import { Router } from "@angular/router";


@Component({
    selector: 'mja-app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        MatButtonModule
    ]
})
export class HomeComponent {

    configuration = inject(AuthAppConfiguration);
    #router = inject(Router);

    redirectToProfilApp() {

		window.location.href = this.configuration.profilUrl;
	}

    gotoOrderTempPassword() {

        this.#router.navigateByUrl('password/temp/order');

    }
}