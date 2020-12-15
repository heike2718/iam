import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators, FormControl } from '@angular/forms';
import { passwortValidator, passwortPasswortWiederholtValidator } from '../shared/validation/app.validators';
import { AppConstants } from '../shared/app.constants';
import { Observable, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ClientInformation, ClientCredentials, RegistrationCredentials, TwoPasswords, AuthSession } from '../shared/model/auth-model';
import { ClientService } from '../services/client.service';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';
import { UserService } from '../services/user.service';
import { AppData } from '../shared/app-data.service';
import { MessagesService, LogService, ResponsePayload } from 'hewi-ng-lib';
import { SessionService } from '../services/session.service';
import { AuthService } from '../services/auth.service';
import { HttpErrorService } from '../error/http-error.service';
import { SignupValidationService } from '../services/signup-valitadion.service';

@Component({
	selector: 'auth-sign-up',
	templateUrl: './sign-up.component.html',
	styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit, OnDestroy {

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

	passwort: AbstractControl;

	passwortWdh: AbstractControl;

	kleber: AbstractControl;

	infoPasswortExpanded = false;

	tooltipPasswort: string;

	submitDisabled: true;

	showClientId: boolean;

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
		private messagesService: MessagesService,
		private logger: LogService,
		private route: ActivatedRoute) { }

	ngOnInit() {

		this.signUpForm = new FormGroup({
			'agbGelesen': new FormControl(false, { 'validators': [Validators.requiredTrue] }),
			'email': new FormControl('', {
				'validators': [Validators.required, Validators.email]
			}),
			'passwort': new FormControl('', { 'validators': [Validators.required, passwortValidator] }),
			'passwortWdh': new FormControl('', { 'validators': [Validators.required, passwortValidator] }),
			'kleber': new FormControl(''),
		}, { 'validators': passwortPasswortWiederholtValidator });

		this.agbGelesen = this.signUpForm.controls['agbGelesen'];
		this.email = this.signUpForm.controls['email'];
		this.passwort = this.signUpForm.controls['passwort'];
		this.passwortWdh = this.signUpForm.controls['passwortWdh'];
		this.kleber = this.signUpForm['kleber'];
		this.tooltipPasswort = AppConstants.tooltips.PASSWORTREGELN;
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

	submitUser(): void {
		this.logger.debug('about to submit ' + this.signUpForm.value);

		this.messagesService.clear();

		const twoPasswords: TwoPasswords = {
			passwort: this.passwort.value,
			passwortWdh: this.passwortWdh.value
		};

		const registrationCredentials: RegistrationCredentials = {
			agbGelesen: this.agbGelesen.value,
			clientCredentials: this.clientCredentials,
			email: this.email.value.trim(),
			kleber: this.kleber ? this.kleber.value : null,
			vorname: this.vorname ? this.vorname.value.trim() : null,
			nachname: this.nachname ? this.nachname.value.trim() : null,
			groups: this.groups,
			nonce: this.nonce,
			// wenn man den loginnamen nicht setzen kann, wird die Mailadresse verwendet.
			loginName: this.loginName ? this.loginName.value.trim() : this.email.value.trim(),
			twoPasswords: twoPasswords
		};

		this.logger.debug(JSON.stringify(registrationCredentials));

		this.userService.registerUser(registrationCredentials, this.session);
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
}
