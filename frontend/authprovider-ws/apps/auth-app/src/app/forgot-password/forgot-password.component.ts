import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators } from '@angular/forms';
import { TempPasswordCredentials, ClientCredentials, AuthSession } from '../shared/model/auth-model';
import { TempPasswordService } from '../services/temp-password.service';
import { HttpErrorService } from '../error/http-error.service';
import { AppData } from '../shared/app-data.service';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { SessionService } from '../services/session.service';
import { BehaviorSubject, Observable, Subject, Subscription } from 'rxjs';
import { ResponsePayload, MessageService } from '@authprovider-ws/common-messages';
import { LogService } from '@authprovider-ws/common-logging';
import { trimString } from '@authprovider-ws/common-components';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';

@Component({
	selector: 'auth-forgot-password',
	templateUrl: './forgot-password.component.html',
	styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {

	@ViewChild('dialogPasswordVersendet')
	dialogPasswordVersendet!: TemplateRef<HTMLElement>;

	orderPwdForm: FormGroup;

	email: AbstractControl;

	kleber: AbstractControl;

	message: string;

	private modalOptions: NgbModalOptions = {
		backdrop:'static',
		centered:true,
		ariaLabelledBy: 'modal-basic-title'
	};

	private authSessionSubscription: Subscription;

	private session: AuthSession;

	private clientCredentials: ClientCredentials;

	constructor(private fb: FormBuilder
		, private tempPwdService: TempPasswordService
		, private authService: AuthService
		, private sessionService: SessionService
		, private httpErrorService: HttpErrorService
		, private messageService: MessageService
		, public appData: AppData
		, private logger: LogService
		, private modalService: NgbModal) {

		this.orderPwdForm = this.fb.group({
			'email': ['', [Validators.required]],
			'kleber': ['']
		});

		this.email = this.orderPwdForm.controls['email'];
		this.kleber = this.orderPwdForm.controls['kleber'];

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

		const _email = this.email.value ? trimString(this.email.value) : null;
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

		this.appData.updateLoading(true);

		const response$ = this.tempPwdService.orderTempPassword(tempPasswordCredentials, this.session);

		response$.subscribe(
			payload => {
				this.appData.updateLoading(false);
				const level = payload.message.level;

				if (level === 'INFO') {
					this.message = payload.message.message;
					this.showDialogSuccess();
				} else {
					this.messageService.error(payload.message.message);
					this.message = '';
				}

			},
			error => this.httpErrorService.handleError(error, 'orderTempPassword', undefined),
			() => {
				this.appData.updateLoading(false);
				this.logger.debug('post call completed');
			}
		);
	}

	showDialogSuccess() {
		this.modalService.open(this.dialogPasswordVersendet, this.modalOptions).result.then((_result) => {			
			this.sessionService.clearSession();
	  });
	}
}
