import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForgotPasswordFacade } from 'apps/auth-app/src/app/forgot-password/api';
import { MatInputModule } from '@angular/material/input';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { REG_EXP_EMAIL, trimFormValues } from '@ap-ws/common-utils';
import { TempPasswordCredentials } from '@auth-app/model';
import { Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'auth-feature',
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

  orderPwdForm!: FormGroup;
  email!: AbstractControl;
  kleber!: AbstractControl;

  #submittingForm = false;
  // #submitSuccess = false;

  #subscriptions: Subscription = new Subscription();

  constructor() {
    this.#createForm();
  }

  ngOnInit(): void {

    const submittingSubscription = this.forgotPasswordFacade.submittingForm$.subscribe((submitting) => this.#submittingForm = submitting);
    // const tempPasswordSuccessSubscription = this.#forgotPasswordFacade.tempPasswordSuccess$.subscribe((success) => this.#submitSuccess = success);

    this.#subscriptions.add(submittingSubscription);
    // this.#subscriptions.add(tempPasswordSuccessSubscription);
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

  cancel() {
    this.#clearForm();
  }

  #createForm() {
    this.orderPwdForm = this.#formBuilder.group({
      email: ['', [Validators.required, Validators.maxLength(255), Validators.pattern(REG_EXP_EMAIL)]],
      kleber: ['']
    });

    this.email = this.orderPwdForm.controls['email'];
    this.kleber = this.orderPwdForm.controls['kleber'];
  }

  #clearForm() {
    setTimeout(() => {
      console.log('Before reset:', this.orderPwdForm.get('email')?.errors);
      this.orderPwdForm.reset();
      console.log('After reset:', this.orderPwdForm.get('email')?.errors);
      this.orderPwdForm.markAsPristine();
      this.orderPwdForm.markAsUntouched();
      // this.orderPwdForm.updateValueAndValidity();
    }, 200);


    // setTimeout(() => {
    //   this.orderPwdForm.markAsPristine();
    //   this.orderPwdForm.markAsUntouched();
    //   this.orderPwdForm.updateValueAndValidity();
    // }, 0);
  };

  #trimAndReadFormValues(): TempPasswordCredentials {
    trimFormValues(this.orderPwdForm);
    return this.orderPwdForm.value;
  }


}
