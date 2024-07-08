import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
	selector: 'prfl-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent {

	constructor(private authService: AuthService) { }


	isLoggedIn(): boolean {
		return this.authService.isLoggedIn();
	}

	logIn(): void {
		this.authService.logIn();
	}

	logOut(): void {
		this.authService.logOut();
	}


}
