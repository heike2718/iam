import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForgotPasswordFacade } from 'apps/auth-app/src/app/forgot-password/api';
import { MatInputModule } from '@angular/material/input';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { REG_EXP_EMAIL } from '@ap-ws/common-utils';

@Component({
  selector: 'auth-feature',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss',
})
export class ForgotPasswordComponent {

  #forgotPasswordFacade = inject(ForgotPasswordFacade)
  #formBuilder = inject(FormBuilder);

  orderPwdForm!: FormGroup;
  email!: AbstractControl;
	kleber!: AbstractControl;

  constructor() {
    this.#createForm();
  }



  #createForm() {
    this.orderPwdForm = this.#formBuilder.group({
      email: ['', [Validators.required, Validators.maxLength(255), Validators.pattern(REG_EXP_EMAIL)]],
      kleber: ['']
    });

    this.email = this.orderPwdForm.controls['email'];
		this.kleber = this.orderPwdForm.controls['kleber'];
  }

  
}
