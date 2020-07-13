import { Component, OnInit, OnDestroy } from '@angular/core';
import { STORAGE_KEY_FULL_NAME } from '../shared/model/app-model';
import { Subscription } from 'rxjs';
import { store } from '../shared/store/app-data';

@Component({
	selector: 'prfl-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

	showLoadingIndicator: boolean;
	private blockingIndicatorSubscription: Subscription;

	constructor() { }

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

		const user = localStorage.getItem(STORAGE_KEY_FULL_NAME);

		if (user !== null) {
			return true;
		}

		return false;
	}


}
