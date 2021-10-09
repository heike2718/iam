import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators, FormControl } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { ClientInformation, ClientCredentials, LoginCredentials, AuthorizationCredentials, AuthSession, isLoginCredentialsValid } from '../shared/model/auth-model';
import { ClientService } from '../services/client.service';
import { UserService } from '../services/user.service';
import { AppData } from '../shared/app-data.service';
import { MessageService, ResponsePayload } from '@authprovider-ws/common-messages';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth.service';
import { SessionService } from '../services/session.service';
import { HttpErrorService } from '../error/http-error.service';
import { LogService } from '@authprovider-ws/common-logging';

@Component({
	selector: 'auth-log-in',
	templateUrl: './log-in.component.html',
	styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit, OnDestroy {

	isDev = environment.envName === 'dev';

	clientInformation$: Observable<ClientInformation>;

	redirectUrl$: Observable<string>;

	private session: AuthSession;

	private clientCredentials: ClientCredentials;

	loginForm: FormGroup;

	gelesenControl: AbstractControl;

	loginName: AbstractControl;

	passwordControl: AbstractControl;

	kleber: AbstractControl;

	showClientId: boolean;

	private redirectUrl = '';

	private redirectSubscription: Subscription;

	private nonce = '';


	constructor(private fb: FormBuilder,
		private clientService: ClientService,
		private userService: UserService,
		private authService: AuthService,
		private sessionService: SessionService,
		private appData: AppData,
		private httpErrorService: HttpErrorService,
		private messageService: MessageService,
		private logger: LogService,
		private router: Router,
		private route: ActivatedRoute) {


		this.loginForm = this.fb.group({
			'gelesen': [false],
			'loginName': ['', [Validators.required, Validators.maxLength(255)]],
			'password': [''],
			'kleber': ['']
		});

		this.gelesenControl = this.loginForm.controls['gelesen'];
		this.loginName = this.loginForm.controls['loginName'];
		this.passwordControl = this.loginForm.controls['password'];
		this.kleber = this.loginForm['kleber'];

		this.showClientId = environment.envName === 'DEV';
	}

	ngOnInit() {
		this.clientInformation$ = this.appData.clientInformation$;
		this.redirectUrl$ = this.appData.redirectUrl$;

		this.loadClientInformation();

		this.redirectUrl$.pipe(
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
	}

	ngOnDestroy() {
		if (this.redirectSubscription) {
			this.redirectSubscription.unsubscribe();
		}
	}

	private loadClientInformation() {

		this.redirectSubscription = this.route.queryParams.pipe(
			filter(params => params.clientId || params.redirectUrl)
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
				this.appData.updateClientCredentials(this.clientCredentials);
				this.clientService.getClient(this.clientCredentials);
				this.authService.createAnonymousSession();
			}
		);
	}

	submitDisabled(): boolean {

		const loginCredentials = this.getSubmitPayload();		
		return !isLoginCredentialsValid(loginCredentials);
	}

	submit(): void {
		this.logger.debug('about to submit ' + this.loginForm.value);

		this.messageService.clear();

		const loginCredentials = this.getSubmitPayload();
		this.logger.debug(JSON.stringify(loginCredentials));
		this.userService.loginUser(loginCredentials, this.session);
	}

	private getSubmitPayload(): LoginCredentials {

		const authCredentials: AuthorizationCredentials = {
			loginName: this.loginName ? this.loginName.value.trim() : null,
			passwort: this.getValueFormControl('password'),
			kleber: this.kleber ? this.kleber.value : null
		};

		const loginCredentials: LoginCredentials = {
			authorizationCredentials: authCredentials,
			clientCredentials: this.clientCredentials,
			nonce: this.nonce
		};

		return loginCredentials;
	} 

	private getValueFormControl(formControlName: string) {
		const val = this.passwordControl.value[formControlName] ? this.passwordControl.value[formControlName] : undefined;
		return val;
	}

	gotoOrderTempPwd(): void {
		this.router.navigateByUrl('password/temp/order');
	}

	private sendRedirect() {
		this.logger.debug('about to redirect to: ' + this.redirectUrl);
		window.location.href = this.redirectUrl;
	}
}

