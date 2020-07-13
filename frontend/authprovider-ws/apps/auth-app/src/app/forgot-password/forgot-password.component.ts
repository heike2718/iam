import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators } from '@angular/forms';
import { TempPasswordCredentials, ClientCredentials, AuthSession } from '../shared/model/auth-model';
import { TempPasswordService } from '../services/temp-password.service';
import { HttpErrorService } from '../error/http-error.service';
import { MessagesService, LogService, ResponsePayload } from 'hewi-ng-lib';
import { AppData } from '../shared/app-data.service';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { SessionService } from '../services/session.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'auth-forgot-password',
	templateUrl: './forgot-password.component.html',
	styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {


	orderPwdForm: FormGroup;

	email: AbstractControl;

	kleber: AbstractControl;

	showMessage: boolean;

	message: string;

	private authSessionSubscription: Subscription;

	private session: AuthSession;

	private clientCredentials: ClientCredentials;

	constructor(private fb: FormBuilder
		, private tempPwdService: TempPasswordService
		, private authService: AuthService
		, private sessionService: SessionService
		, private httpErrorService: HttpErrorService
		, private messagesService: MessagesService
		, private appData: AppData
		, private logger: LogService
		, private router: Router) {

		this.orderPwdForm = this.fb.group({
			'email': ['', [Validators.required]],
			'kleber': ['']
		});

		this.email = this.orderPwdForm.controls['email'];
		this.kleber = this.orderPwdForm.controls['kleber'];

		this.showMessage = false;
		this.message = '';
	}

	ngOnInit() {
		this.clientCredentials = this.appData.clientCredentialsSubject.getValue();

		this.authService.createAnonymousSession().subscribe(
			(respPayload: ResponsePayload) => {
				this.session = respPayload.data;
				this.sessionService.setSession(this.session);

			},
			error => this.httpErrorService.handleError(error, 'createAnonymousSession', null)
		);
	}

	ngOnDestroy() {
		if (this.authSessionSubscription) {
			this.authSessionSubscription.unsubscribe();
		}
	}

	submit(): void {

		const _email = this.email.value ? this.email.value.trim() : null;
		const _kleber = this.kleber.value ? this.kleber.value : null;

		let _clientCreds = this.clientCredentials;
		if (!this.clientCredentials || this.clientCredentials.accessToken === 'undefined') {
			_clientCreds = null;
		}

		const tempPasswordCredentials: TempPasswordCredentials = {
			email: _email,
			clientCredentials: _clientCreds,
			kleber: _kleber
		};

		this.logger.debug(JSON.stringify(tempPasswordCredentials));
		const response$ = this.tempPwdService.orderTempPassword(tempPasswordCredentials, this.session);

		response$.subscribe(
			payload => {
				const level = payload.message.level;

				if (level === 'INFO') {
					this.showMessage = true;
					this.message = payload.message.message;
					this.sessionService.clearSession();
				} else {
					this.showMessage = false;
					this.messagesService.error(payload.message.message);
					this.message = '';
				}

			},
			error => this.httpErrorService.handleError(error, 'orderTempPassword', undefined),
			() => this.logger.debug('post call completed')
		);
	}

	closeModal(): void {
		this.showMessage = false;
		this.router.navigateByUrl('/home');
	}

}
