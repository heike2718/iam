import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { BehaviorSubject, Observable, Subject, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { TempPasswordService } from '../services/temp-password.service';
import { HttpErrorService } from '../error/http-error.service';
import { MessageService, ResponsePayload } from '@authprovider-ws/common-messages';
import { ChangeTempPasswordPayload, ClientInformation, TwoPasswords, AuthSession } from '../shared/model/auth-model';
import { AuthService } from '../services/auth.service';
import { SessionService } from '../services/session.service';
import { LogService } from '@authprovider-ws/common-logging';
import { PASSWORTREGELN, modalOptions, trimString } from '@authprovider-ws/common-components';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AppData } from '../shared/app-data.service';

@Component({
	selector: 'auth-temp-password',
	templateUrl: './temp-password.component.html',
	styleUrls: ['./temp-password.component.css']
})
export class TempPasswordComponent implements OnInit, OnDestroy {

	@ViewChild('dialogPasswordRules')
	dialogPasswordRules!: TemplateRef<HTMLElement>;

	@ViewChild('successDialog')
	successDialog!: TemplateRef<HTMLElement>;

	queryParams$: Observable<Params>;

	private tokenId: string;

	private authSessionSubscription: Subscription;

	private session: AuthSession;

	private clientInformation: ClientInformation;

	changePwdForm: FormGroup;

	email: AbstractControl;

	tempPassword: AbstractControl;

	tooltipPasswort: string;

	submitDisabled: true;

	message: string;

	zurueckText: string;

	private queryParamsSubscription: Subscription;

	constructor(private fb: FormBuilder,
		private logger: LogService,
		private tempPwdService: TempPasswordService,
		private authService: AuthService,
		public appData: AppData,
		private sessionService: SessionService,
		private httpErrorService: HttpErrorService,
		private modalService: NgbModal,
		private messageService: MessageService,
		private route: ActivatedRoute,
		private router: Router) {


		this.tooltipPasswort = PASSWORTREGELN;
		this.tokenId = 'undefined';
		this.message = '';
		this.zurueckText = 'zurück';

		this.changePwdForm = this.fb.group({
			'email': ['', [Validators.required, Validators.email]],
			'tempPassword': ['', [Validators.required]],
			'doublePassword': new FormControl('')
		});

		this.email = this.changePwdForm.controls['email'];
		this.tempPassword = this.changePwdForm.controls['tempPassword'];
	}

	ngOnInit() {

		this.queryParams$ = this.route.queryParams;

		this.authService.createAnonymousSession().subscribe(
			(respPayload: ResponsePayload) => {
				this.session = respPayload.data;
				this.sessionService.setSession(this.session);

			},
			error => this.httpErrorService.handleError(error, 'createAnonymousSession', null)
		);

		this.queryParamsSubscription = this.queryParams$.pipe(
			filter(params => params.tokenId)
		).subscribe(
			params => {
				this.tokenId = params.tokenId;

				if (!this.tokenId || this.tokenId === 'undefined') {
					// tslint:disable-next-line:max-line-length
					this.message = 'Der aufgerufene Link ist ungültig. Bitte kopieren Sie den Link vollständig oder klicken Sie ihn nochmals an. Falls das nicht hilft, senden Sie bitte eine Mail an minikaenguru@egladil.de.';
				}
			}
		);
	}


	ngOnDestroy() {
		if (this.queryParamsSubscription) {
			this.queryParamsSubscription.unsubscribe();
		}
		if (this.authSessionSubscription) {
			this.authSessionSubscription.unsubscribe();
		}
	}

	submit() {

		const _email = this.email.value ? trimString(this.email.value) : null;
		const _tempPassword = this.tempPassword.value ? trimString(this.tempPassword.value) : null;

		const _twoPasswords: TwoPasswords = this.getTwoPasswords();

		const credentials: ChangeTempPasswordPayload = {
			tokenId: this.tokenId,
			tempPassword: _tempPassword,
			email: _email,
			twoPasswords: _twoPasswords

		};

		this.appData.updateLoading(true);

		const response$ = this.tempPwdService.changeTempPassword(credentials, this.session);

		response$.subscribe(
			payload => {
				const level = payload.message.level;

				this.appData.updateLoading(false);

				if (level === 'INFO') {
					this.sessionService.clearSession();					
					if (payload.data) {
						this.clientInformation = payload.data;
						this.zurueckText = this.clientInformation.zurueckText;
					}
					this.openDialogPasswordChanged();

				} else {
					this.messageService.error(payload.message.message);
					this.message = '';
				}

			},
			error => {
				this.appData.updateLoading(false);
				this.resetForm();
				this.httpErrorService.handleError(error, 'changeTempPassword', undefined);
			},
			() => this.logger.debug('post call completed')
		);
	}

	private resetForm(): void {
		this.changePwdForm.reset();
	}

	canRedirect(): boolean {
		return this.clientInformation !== undefined;
	}

	openDialogPasswordRules(): void {
		this.modalService.open(this.dialogPasswordRules, modalOptions).result.then((_result) => {
			
			// do nothing
	  });
	}

	openDialogPasswordChanged(): void {
		this.modalService.open(this.successDialog, modalOptions).result.then((_result) => {
			
			this.closeModal();
	  });
	}	


	closeModal(): void {
		if (this.canRedirect()) {
			this.sendRedirect();
		} else {
			this.router.navigateByUrl('/home');
		}
	}


	private sendRedirect(): void {
		window.location.href = this.clientInformation.baseUrl;
	}

	private getTwoPasswords(): TwoPasswords  | undefined{

		const val: FormControl = this.changePwdForm.value['doublePassword'] ? this.changePwdForm.value['doublePassword'] : undefined;

		if (val) {
			const result: TwoPasswords = {
				passwort: val['firstPassword'],
				passwortWdh: val['secondPassword']
			};
			return result;
		}

		return undefined;
	}
}

