import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { User } from '../model/profil.model';
import * as _ from 'lodash';


export const initialUser: User = {
	loginName: '',
	email: '',
	vorname: '',
	nachname: ''
};

@Injectable({
	providedIn: 'root'
})
export class DataStore {

	private userSubject = new BehaviorSubject<User>(initialUser);

	private authSignUpOutcomeSubject = new BehaviorSubject<boolean>(false);

	private clientAccessTokenSubject = new BehaviorSubject<string>('');

	private blockingIndicatorSubject = new BehaviorSubject<boolean>(false);

	private csrfTokenSubject = new BehaviorSubject<string>('');

	private apiVersionSubject = new BehaviorSubject<string>('');

	private errorResponseSubject = new BehaviorSubject<boolean>(false);

	user$: Observable<User> = this.userSubject.asObservable();

	authSignUpOutcome$: Observable<boolean> = this.authSignUpOutcomeSubject.asObservable();

	clientAccessToken$: Observable<string> = this.clientAccessTokenSubject.asObservable();

	blockingIndicator$: Observable<boolean> = this.blockingIndicatorSubject.asObservable();

	csrfToken$: Observable<string> = this.csrfTokenSubject.asObservable();

	apiVersion$: Observable<string> = this.apiVersionSubject.asObservable();

	responseError$: Observable<boolean> = this.errorResponseSubject.asObservable();


	updateAuthSignUpOutcome(success: boolean) {
		this.authSignUpOutcomeSubject.next(success);
	}

	updateBlockingIndicator(show: boolean) {
		this.blockingIndicatorSubject.next(show);
	}

	initUser(user: User): void {
		this.userSubject.next(_.cloneDeep(user));
	}

	clearUser(): void {
		this.userSubject.next(initialUser);
	}

	updateClientAccessToken(token: string) {
		this.clientAccessTokenSubject.next(token);
	}

	initCsrfToken(token: string) {
		this.csrfTokenSubject.next(token);
	}

	updateApiVersion(version: string) {
		this.apiVersionSubject.next(version);
	}

	updateErrorStatus(error: boolean) {
		this.errorResponseSubject.next(error);
	}
}

export const store = new DataStore();
