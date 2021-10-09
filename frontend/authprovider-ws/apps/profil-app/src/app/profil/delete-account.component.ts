import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { User } from '../shared/model/profil.model';
import { store } from '../shared/store/app-data';
import { UserService } from '../services/user.service';
import { HttpErrorService } from '../error/http-error.service';
import { FormGroup, AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { SessionService } from '../services/session.service';

@Component({
	selector: 'prfl-delete-account',
	templateUrl: './delete-account.component.html',
	styleUrls: ['./delete-account.component.css']
})
export class DeleteAccountComponent implements OnInit, OnDestroy {


	user$: Observable<User>;

	deleteAccountForm: FormGroup;

	checkAccept: AbstractControl;

	private cachedUser: User;

	private userSubscription: Subscription;

	private blockingIndicatorSubscription: Subscription;

	private csrfTokenSubscription: Subscription;

	private csrfToken = '';

	showBlockingIndicator: boolean;

	showDialog = false;

	constructor(private fb: FormBuilder
		, private userService: UserService
		, private sessionService: SessionService
		, private httpErrorService: HttpErrorService) {

		this.deleteAccountForm = this.fb.group({
			checkAccept: [false, [Validators.required, Validators.requiredTrue]]
		});

		this.checkAccept = this.deleteAccountForm.controls.checkAccept;
	}

	ngOnInit() {

		this.user$ = store.user$;

		this.userSubscription = this.user$.subscribe(
			user => {
				this.cachedUser = user;
			}
		);

		this.blockingIndicatorSubscription = store.blockingIndicator$.subscribe(
			value => this.showBlockingIndicator = value
		);

		this.csrfTokenSubscription = store.csrfToken$.subscribe(
			token => this.csrfToken = token
		);

	}

	ngOnDestroy() {
		if (this.userSubscription) {
			this.userSubscription.unsubscribe();
		}
		if (this.blockingIndicatorSubscription) {
			this.blockingIndicatorSubscription.unsubscribe();
		}
		if (this.csrfTokenSubscription) {
			this.csrfTokenSubscription.unsubscribe();
		}
	}

	submit(): void {

		this.userService.deleteAccount(this.csrfToken).subscribe(
			_payload => {
				store.updateBlockingIndicator(false);
				this.showDialog = true;
			},
			(error => {
				store.updateBlockingIndicator(false);
				this.httpErrorService.handleError(error, 'deleteAccount');
			})
		);
	}

	closeModal(): void {
		this.sessionService.clearSession();
	}

}
