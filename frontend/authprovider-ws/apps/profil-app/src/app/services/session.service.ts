import { Injectable } from '@angular/core';
import { store } from '../shared/store/app-data';
import { Router } from '@angular/router';
import { STORAGE_KEY_FULL_NAME
	, STORAGE_KEY_SESSION_EXPIRES_AT
	, STORAGE_KEY_DEV_SESSION_ID
	, STORAGE_KEY_ID_REFERENCE } from '../shared/model/profil.model';
import { LogService } from '@authprovider-ws/common-logging';

@Injectable({
	providedIn: 'root'
})
export class SessionService {

	constructor(private router: Router
		, private logger: LogService) { }


	clearSession() {
		localStorage.removeItem(STORAGE_KEY_FULL_NAME);
		localStorage.removeItem(STORAGE_KEY_SESSION_EXPIRES_AT);
		localStorage.removeItem(STORAGE_KEY_DEV_SESSION_ID);
		localStorage.removeItem(STORAGE_KEY_ID_REFERENCE);
		store.updateAuthSignUpOutcome(false);
		store.clearUser();
		this.router.navigateByUrl('/home');
	}

	sessionExpired(): boolean {

		this.logger.debug('check session');

		// session expires at ist in Millisekunden seit 01.01.1970
		const expiration = localStorage.getItem(STORAGE_KEY_SESSION_EXPIRES_AT);
		if (expiration === null) {
			return true;
		}
		const expiredAt: number = JSON.parse(expiration);
		const now = new Date().getMilliseconds();

		return now > expiredAt;
	}
}
