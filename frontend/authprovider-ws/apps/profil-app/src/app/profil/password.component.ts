import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators } from '@angular/forms';
import { AppConstants } from '../shared/app.constants';
import { passwortValidator, passwortPasswortWiederholtValidator } from '../shared/validation/app.validators';
import { Observable, Subscription } from 'rxjs';
import { User, TwoPasswords, ChangePasswordPayload } from '../shared/model/app-model';
import { store } from '../shared/store/app-data';
import { UserService } from '../services/user.service';

@Component({
	selector: 'prfl-password',
	templateUrl: './password.component.html',
	styleUrls: ['./password.component.css']
})
export class PasswordComponent implements OnInit, OnDestroy {

	user$: Observable<User>;

	changePwdForm: FormGroup;

	oldPassword: AbstractControl;

	passwort: AbstractControl;

	passwortWdh: AbstractControl;

	tooltipPasswort: string;

	private blockingIndicatorSubscription: Subscription;

	private userSubscription: Subscription;

	private csrfTokenSubscription: Subscription;

	private csrfToken = '';

	private cachedUser: User;

	showBlockingIndicator: boolean;

	constructor(private fb: FormBuilder
		, private userService: UserService) {

		this.tooltipPasswort = AppConstants.tooltips.PASSWORTREGELN;

		this.changePwdForm = this.fb.group({
			oldPassword: ['', [Validators.required, passwortValidator]],
			passwort: ['', [Validators.required, passwortValidator]],
			passwortWdh: ['', [Validators.required, passwortValidator]]
		}, { validator: passwortPasswortWiederholtValidator });

		this.oldPassword = this.changePwdForm.controls.oldPassword;
		this.passwort = this.changePwdForm.controls.passwort;
		this.passwortWdh = this.changePwdForm.controls.passwortWdh;

	}

	ngOnInit() {
		this.user$ = store.user$;

		this.userSubscription = this.user$.subscribe(
			user => {
				this.cachedUser = user;
			}

		);

		this.csrfTokenSubscription = store.csrfToken$.subscribe(
			token => {
				this.csrfToken = token;
			}
		);


		this.blockingIndicatorSubscription = store.blockingIndicator$.subscribe(
			value => this.showBlockingIndicator = value
		);
	}

	ngOnDestroy() {
		if (this.blockingIndicatorSubscription) {
			this.blockingIndicatorSubscription.unsubscribe();
		}
		if (this.userSubscription) {
			this.userSubscription.unsubscribe();
		}
		if (this.csrfTokenSubscription) {
			this.csrfTokenSubscription.unsubscribe();
		}
	}

	submit(): void {

		const _twoPasswords: TwoPasswords = {
			passwort: this.passwort.value,
			passwortWdh: this.passwortWdh.value
		};

		const credentials: ChangePasswordPayload = {
			passwort: this.oldPassword.value,
			twoPasswords: _twoPasswords
		};

		this.changePwdForm.reset();
		this.userService.changePassword(credentials, this.cachedUser, this.csrfToken);
	}
}

