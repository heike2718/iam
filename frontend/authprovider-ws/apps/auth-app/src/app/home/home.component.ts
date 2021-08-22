import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';

@Component({
	selector: 'auth-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

	// Beispiel für path parameters: https://scotch.io/tutorials/handling-route-parameters-in-angular-v2

	modusDev = !environment.production;

	profilAppUrl = environment.profilUrl;

	constructor() { }

	ngOnInit() {
	}

	redirectToProfilApp() {

		window.location.href = this.profilAppUrl;
	}

}
