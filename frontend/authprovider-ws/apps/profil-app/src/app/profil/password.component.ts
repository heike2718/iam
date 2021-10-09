import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, Validators, FormControl } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { User, TwoPasswords, ChangePasswordPayload, isChangePasswordPayloadValid } from '../shared/model/profil.model';
import { store } from '../shared/store/app-data';
import { UserService } from '../services/user.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { modalOptions } from '@authprovider-ws/common-components';
import { PASSWORTREGELN, sonderzeichen } from 'libs/common-components/src/lib/commons-component.model';
import { LogService } from '@authprovider-ws/common-logging';
import { DoublePasswordsPayload } from 'libs/common-components/src/lib/validation/app.validators';

@Component({
	selector: 'prfl-password',
	templateUrl: './password.component.html',
	styleUrls: ['./password.component.css']
})
export class PasswordComponent implements OnInit, OnDestroy {	

	@ViewChild('dialogPasswordRules')
	dialogPasswordRules!: TemplateRef<HTMLElement>;

	user$: Observable<User>;

	changePwdForm: FormGroup;

	tooltipPasswort: string  = PASSWORTREGELN;

	theSonderzeichen: string  = sonderzeichen;

	private blockingIndicatorSubscription: Subscription;

	private userSubscription: Subscription;

	private csrfTokenSubscription: Subscription;

	private csrfToken = '';

	private cachedUser: User;

	showBlockingIndicator: boolean;

	constructor(private fb: FormBuilder
		, private userService: UserService
		, private logger: LogService
		, private modalService: NgbModal) {

		this.tooltipPasswort

		this.changePwdForm = this.fb.group({
			'password': new FormControl(''),
			'doublePassword': new FormControl('')
		});
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


	submitDisabled(): boolean {

		const credentials: ChangePasswordPayload = this.getSubmitPayload();

		if (!credentials) {		
			return true;
		}		

		return !isChangePasswordPayloadValid(credentials);
	}

	submit(): void {

		const credentials: ChangePasswordPayload = this.getSubmitPayload();

		if (!credentials) {
			this.logger.debug('payload was undefined');
		}

		this.changePwdForm.reset();
		this.userService.changePassword(credentials, this.cachedUser, this.csrfToken);
	}

	openDialogPasswordRules(): void {
		this.modalService.open(this.dialogPasswordRules, modalOptions).result.then((_result) => {
			
			// do nothing
	  });
	}

	private getSubmitPayload(): ChangePasswordPayload | undefined {

		const _twoPasswords = this.getTwoPasswords();	
		if (!_twoPasswords) {
			return undefined;
		}
		

		const result: ChangePasswordPayload = {
			passwort: this.getThePassword(),
			twoPasswords: _twoPasswords
		};

		return result;
	}

	private getThePassword(): string {
		const val = this.changePwdForm.value['password'] ? this.changePwdForm.value['password'] : undefined;

		if (val) {
			return val['password'];
		}

		return '';
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

