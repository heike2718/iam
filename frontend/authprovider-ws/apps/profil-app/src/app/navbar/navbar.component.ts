import { Component, ViewChild } from '@angular/core';
import { NgbCollapse } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../services/auth.service';
import { STORAGE_KEY_ID_REFERENCE } from '../shared/model/profil.model';

@Component({
	selector: 'prfl-navbar',
	templateUrl: './navbar.component.html',
	styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

	isCollapsed = true;

	@ViewChild(NgbCollapse, { static: true }) navbarToggler: NgbCollapse;

	constructor(private authService: AuthService) { }

	collapseNav() {
		if (this.navbarToggler) {
			this.isCollapsed = true;
		}
	}

	isLoggedIn(): boolean {
		return !this.isLoggedOut();
	}

	isLoggedOut(): boolean {
		const idReference = localStorage.getItem(STORAGE_KEY_ID_REFERENCE);
		return !idReference;
	}

	logout() {
		this.authService.logOut();

		// this.sessionService.clearSession();
	}

	login() {
		this.authService.logIn();
	}

}
