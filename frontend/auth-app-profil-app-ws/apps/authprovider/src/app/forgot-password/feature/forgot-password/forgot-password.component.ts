import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForgotPasswordFacade } from 'apps/authprovider/src/app/forgot-password/api';
import { MatInputModule } from '@angular/material/input';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { REG_EXP_EMAIL, trimFormValues } from '@ap-ws/common-utils';
import { TempPasswordCredentials } from '@authprovider/model';
import { Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { MatDialog } from "@angular/material/dialog";
import { InfoDialogComponent } from '@ap-ws/common-ui';

@Component({
  selector: 'auth-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss',
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {

  forgotPasswordFacade = inject(ForgotPasswordFacade)
  #formBuilder = inject(FormBuilder);
  #router = inject(Router);

  orderPwdForm!: FormGroup;
  email!: AbstractControl;
  kleber!: AbstractControl;

  #submittingForm = false;

  #subscriptions: Subscription = new Subscription();

  constructor(public confirmDeleteDialog: MatDialog) {
    this.#createForm();
  }

  ngOnInit(): void {

    const submittingSubscription = this.forgotPasswordFacade.submittingForm$.subscribe((submitting) => this.#submittingForm = submitting);
    const tempPasswordSuccessMessageSubscription = this.forgotPasswordFacade.tempPasswordSuccessMessage$.subscribe((message) => {
      if (message) {
        this.#showSuccessDialog(message);
      }
    })
    this.#subscriptions.add(submittingSubscription);
    this.#subscriptions.add(tempPasswordSuccessMessageSubscription);
  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
    this.forgotPasswordFacade.onDestroyComponent();
  }

  buttonSubmitDisabled(): boolean {

    if (!this.orderPwdForm.valid || this.#submittingForm ) {
      return true;
    }

    return false;

  }
  submit(): void {

    if (this.orderPwdForm.valid) {
      const tempPasswordCredentials = this.#trimAndReadFormValues();
      this.forgotPasswordFacade.orderTempPassword(tempPasswordCredentials);       
    }
  }

  gotoStartseite() {
    this.#router.navigateByUrl('/home');
  }

  #showSuccessDialog(message: string) {

    const dialogRef = this.confirmDeleteDialog.open(InfoDialogComponent, {
      width: '500px',
      disableClose: true,
      data: {
        title: 'Einmalpasswort versendet',
        text: message
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.gotoStartseite();
      }
    });
  }

  #createForm() {
    this.orderPwdForm = this.#formBuilder.group({
      email: ['', [Validators.required, Validators.maxLength(255), Validators.pattern(REG_EXP_EMAIL)]],
      kleber: ['']
    });

    this.email = this.orderPwdForm.controls['email'];
    this.kleber = this.orderPwdForm.controls['kleber'];
  }

  #trimAndReadFormValues(): TempPasswordCredentials {
    trimFormValues(this.orderPwdForm);
    return this.orderPwdForm.value;
  }


}
