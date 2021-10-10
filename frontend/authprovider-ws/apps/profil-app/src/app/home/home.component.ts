import { Component, OnInit, OnDestroy } from '@angular/core';
import { STORAGE_KEY_FULL_NAME } from '../shared/model/profil.model';
import { Subscription } from 'rxjs';
import { store } from '../shared/store/app-data';
import { AuthService } from '../services/auth.service';

@Component({
	selector: 'prfl-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

	showLoadingIndicator: boolean;
	private blockingIndicatorSubscription: Subscription;

	constructor(private authService: AuthService) { }

	ngOnInit() {

		this.blockingIndicatorSubscription = store.blockingIndicator$.subscribe(
			bl => this.showLoadingIndicator = bl
		);

	}

	ngOnDestroy() {
		if (this.blockingIndicatorSubscription) {
			this.blockingIndicatorSubscription.unsubscribe();
		}
	}

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
