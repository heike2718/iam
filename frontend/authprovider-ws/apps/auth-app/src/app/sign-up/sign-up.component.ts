import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators, FormControl } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ClientInformation, ClientCredentials, RegistrationCredentials, TwoPasswords, AuthSession, isRegistrationPayloadValid } from '../shared/model/auth-model';
import { ClientService } from '../services/client.service';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';
import { UserService } from '../services/user.service';
import { AppData } from '../shared/app-data.service';
import { MessageService,  ResponsePayload } from '@authprovider-ws/common-messages';
import { SessionService } from '../services/session.service';
import { AuthService } from '../services/auth.service';
import { HttpErrorService } from '../error/http-error.service';
import { SignupValidationService } from '../services/signup-valitadion.service';
import { LogService } from '@authprovider-ws/common-logging';
import { PASSWORTREGELN, modalOptions, trimString } from '@authprovider-ws/common-components';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
	selector: 'auth-sign-up',
	templateUrl: './sign-up.component.html',
	styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit, OnDestroy {

	@ViewChild('dialogPasswordRules')
	dialogPasswordRules!: TemplateRef<HTMLElement>;

	isDev = environment.envName === 'dev';

	clientInformation$: Observable<ClientInformation>;

	redirectUrl$: Observable<string>;

	private session: AuthSession;

	private clientCredentials: ClientCredentials;

	private groups: string;

	private nonce = '';

	signUpForm: FormGroup;

	agbGelesen: AbstractControl;

	loginName: AbstractControl;

	vorname: AbstractControl;

	nachname: AbstractControl;

	email: AbstractControl;

	kleber: AbstractControl;

	tooltipPasswort: string;

	showClientId: boolean;

	datenschutzUrl: string = environment.datenschutzUrl;

	private redirectUrl = '';

	private redirectSubscription: Subscription;

	private clientInfoSubscription: Subscription;

	constructor(private fb: FormBuilder,
		private clientService: ClientService,
		private userService: UserService,
		private authService: AuthService,
		private sessionService: SessionService,
		private validationService: SignupValidationService,
		private appData: AppData,
		private httpErrorService: HttpErrorService,
		private modalService: NgbModal,
		private messageService: MessageService,
		private logger: LogService,
		private route: ActivatedRoute) { }

	ngOnInit() {

		this.signUpForm = new FormGroup({
			'agbGelesen': new FormControl(false, { 'validators': [Validators.requiredTrue] }),
			'email': new FormControl('', {
				'validators': [Validators.required, Validators.email]
			}),
			'doublePassword': new FormControl(''),
			'kleber': new FormControl(''),
		});

		this.agbGelesen = this.signUpForm.controls['agbGelesen'];
		this.email = this.signUpForm.controls['email'];
		this.kleber = this.signUpForm['kleber'];
		this.tooltipPasswort = PASSWORTREGELN;
		this.showClientId = environment.envName === 'DEV';


		this.clientInformation$ = this.appData.clientInformation$;
		this.redirectUrl$ = this.appData.redirectUrl$;

		this.loadClientInformation();

		this.redirectSubscription = this.redirectUrl$.pipe(
			filter(str => str.length > 0)
		).subscribe(
			str => {
				this.redirectUrl = str;
				this.sendRedirect();
			}
		);

		this.authService.createAnonymousSession().subscribe(
			(respPayload: ResponsePayload) => {
				this.session = respPayload.data;
				this.sessionService.setSession(this.session);

			},
			error => this.httpErrorService.handleError(error, 'createAnonymousSession', null)
		);

		this.clientInfoSubscription = this.clientInformation$.subscribe(
			info => {
				if (info.loginnameSupported) {
					this.signUpForm.addControl(
						'loginName', new FormControl('', {
							'validators': [Validators.required, Validators.maxLength(255)],
							'asyncValidators': [this.forbiddenLoginName.bind(this)],
							'updateOn': 'blur'
						})
					);
					this.loginName = this.signUpForm.controls['loginName'];
				}
				if (info.namenRequired) {
					this.signUpForm.addControl(
						'vorname', new FormControl('', [Validators.required, Validators.maxLength(100)])
					);
					this.vorname = this.signUpForm.controls['vorname'];
					this.signUpForm.addControl(
						'nachname', new FormControl('', [Validators.required, Validators.maxLength(100)])
					);
					this.nachname = this.signUpForm.controls['nachname'];
				}
			}
		);
	}


	ngOnDestroy() {
		if (this.redirectSubscription) {
			this.redirectSubscription.unsubscribe();
		}
		if (this.clientInfoSubscription) {
			this.clientInfoSubscription.unsubscribe();
		}
	}

	openDialogPasswordRules(): void {
		this.modalService.open(this.dialogPasswordRules, modalOptions).result.then((_result) => {
			
			// do nothing
	  });
	}

	private loadClientInformation() {

		this.redirectSubscription = this.route.queryParams.pipe(
			filter(params => params.clientId || params.redirectUrl || params.nonce || params.groups)
		).subscribe(
			params => {
				this.clientCredentials = {
					accessToken: params.accessToken,
					redirectUrl: params.redirectUrl,
					state: params.state
				};
				if (params.nonce) {
					this.nonce = params.nonce;
				}
				if (params.groups) {
					this.groups = params.groups + ',STANDARD';
				} else {
					this.groups = 'STANDARD';
				}
				this.appData.updateClientCredentials(this.clientCredentials);
				this.clientService.getClient(this.clientCredentials);
			}
		);
	}

	submitDisabled(): boolean {

		const registrationCredentials: RegistrationCredentials = this.getRegistrationCredentials();
		return !isRegistrationPayloadValid(registrationCredentials);
		// return false;
	}

	submitUser(): void {
		this.logger.debug('about to submit ' + this.signUpForm.value);

		this.messageService.clear();
		const registrationCredentials: RegistrationCredentials = this.getRegistrationCredentials();
		this.logger.debug(JSON.stringify(registrationCredentials));
		this.userService.registerUser(registrationCredentials, this.session);
	}

	getValueAgbGelesen(): boolean {
		return this.agbGelesen.value;
	}

	private sendRedirect() {
		this.logger.debug('about to redirect to: ' + this.redirectUrl);
		window.location.href = this.redirectUrl;
	}



	forbiddenLoginName(control: FormControl): Promise<any> | Observable<any> {
		const promise = new Promise<any>((resolve, _reject) => {
			const loginName = control.value;

			if (this.session.csrfToken && this.session.csrfToken.length > 0) {

				this.validationService.validate('loginname', loginName, this.session).pipe(
					map(res => <ResponsePayload>res)
				).subscribe(
					payload => {
						const messagePayload = payload.message;

						if ('ERROR' === messagePayload.level) {
							resolve({ 'loginNameKnown': true });
						} else {
							resolve(null);
						}
					},
					error => {
						this.logger.debug(error);
						resolve(null);
					}
				);
			} else {
				resolve(null);
			}
		});
		return promise;
	}

	private getRegistrationCredentials(): RegistrationCredentials | undefined {

		const emailVal = this.email ? trimString(this.email.value) : null;

		const twoPasswords: TwoPasswords = this.getTwoPasswords();

		const registrationCredentials: RegistrationCredentials = {
			agbGelesen: this.getValueAgbGelesen(),
			clientCredentials: this.clientCredentials,
			email: emailVal,
			kleber: this.kleber ? this.kleber.value : null,
			vorname: this.vorname ? trimString(this.vorname.value) : null,
			nachname: this.nachname ? trimString(this.nachname.value) : null,
			groups: this.groups,
			nonce: this.nonce,
			// wenn man den loginnamen nicht setzen kann, wird die Mailadresse verwendet.
			loginName: this.loginName ? trimString(this.loginName.value) : emailVal,
			twoPasswords: twoPasswords
		};

		return registrationCredentials;
	}

	private getTwoPasswords(): TwoPasswords  | undefined{

		const val: FormControl = this.signUpForm.value['doublePassword'] ? this.signUpForm.value['doublePassword'] : undefined;

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
