import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment.prod';

@Component({
	selector: 'auth-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

	// Beispiel für path parameters: https://scotch.io/tutorials/handling-route-parameters-in-angular-v2

	constructor() { }

	ngOnInit() {
	}

	redirectToProfilApp() {

		const url = environment.profilUrl;
		window.location.href = url;
	}

}
