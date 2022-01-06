import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { User } from '../shared/model/profil.model';
import { store } from '../shared/store/app-data';
import { UserService } from '../services/user.service';
import { HttpErrorService } from '../error/http-error.service';
import { FormGroup, AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { SessionService } from '../services/session.service';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';

@Component({
	selector: 'prfl-delete-account',
	templateUrl: './delete-account.component.html',
	styleUrls: ['./delete-account.component.css']
})
export class DeleteAccountComponent implements OnInit, OnDestroy {

	

	@ViewChild('dialogAccountDeleted')
	dialogAccountDeleted!: TemplateRef<HTMLElement>;


	user$: Observable<User>;

	deleteAccountForm: FormGroup;

	checkAccept: AbstractControl;

	private modalOptions: NgbModalOptions = {
		backdrop:'static',
		centered:true,
		ariaLabelledBy: 'modal-basic-title'
	};

	private userSubscription: Subscription;

	private blockingIndicatorSubscription: Subscription;

	private csrfTokenSubscription: Subscription;

	private csrfToken = '';

	showBlockingIndicator: boolean;

	

	constructor(private fb: FormBuilder
		, private userService: UserService
		, private sessionService: SessionService
		, private modalService: NgbModal
		, private httpErrorService: HttpErrorService) {

		this.deleteAccountForm = this.fb.group({
			checkAccept: [false, [Validators.required, Validators.requiredTrue]]
		});

		this.checkAccept = this.deleteAccountForm.controls.checkAccept;
	}

	ngOnInit() {

		this.user$ = store.user$;

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
				this.showDialogSuccess();
			},
			(error => {
				store.updateBlockingIndicator(false);
				this.httpErrorService.handleError(error, 'deleteAccount');
			})
		);
	}

	showDialogSuccess() {
		this.modalService.open(this.dialogAccountDeleted, this.modalOptions).result.then((_result) => {
			
			this.sessionService.clearSession();
	  });
	}

	closeModal(): void {
		this.sessionService.clearSession();
	}

}
