import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { ClientInformation, ClientCredentials, LoginCredentials, AuthorizationCredentials, AuthSession } from '../shared/model/auth-model';
import { ClientService } from '../services/client.service';
import { UserService } from '../services/user.service';
import { AppData } from '../shared/app-data.service';
import { MessagesService, LogService, ResponsePayload } from 'hewi-ng-lib';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { passwortValidator } from '../shared/validation/app.validators';
import { AuthService } from '../services/auth.service';
import { SessionService } from '../services/session.service';
import { HttpErrorService } from '../error/http-error.service';

@Component({
	selector: 'auth-log-in',
	templateUrl: './log-in.component.html',
	styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit, OnDestroy {

	clientInformation$: Observable<ClientInformation>;

	redirectUrl$: Observable<string>;

	private session: AuthSession;

	private clientCredentials: ClientCredentials;

	loginForm: FormGroup;

	loginName: AbstractControl;

	passwort: AbstractControl;

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
		private messagesService: MessagesService,
		private logger: LogService,
		private router: Router,
		private route: ActivatedRoute) {


		this.loginForm = this.fb.group({
			'loginName': ['', [Validators.required]],
			'passwort': ['', [Validators.required, passwortValidator]],
			'kleber': ['']
		});

		this.loginName = this.loginForm.controls['loginName'];
		this.passwort = this.loginForm.controls['passwort'];
		this.kleber = this.loginForm['kleber'];

		this.showClientId = !environment.production;
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

	submit(): void {
		this.logger.debug('about to submit ' + this.loginForm.value);

		this.messagesService.clear();

		const authCredentials: AuthorizationCredentials = {
			loginName: this.loginName ? this.loginName.value.trim() : null,
			passwort: this.passwort.value,
			kleber: this.kleber ? this.kleber.value : null
		};

		const loginCredentials: LoginCredentials = {
			authorizationCredentials: authCredentials,
			clientCredentials: this.clientCredentials,
			nonce: this.nonce
		};

		this.logger.debug(JSON.stringify(loginCredentials));

		this.userService.loginUser(loginCredentials, this.session);
	}

	gotoOrderTempPwd(): void {
		this.router.navigateByUrl('password/temp/order');
	}

	private sendRedirect() {
		this.logger.debug('about to redirect to: ' + this.redirectUrl);
		window.location.href = this.redirectUrl;
	}
}

