import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { REG_EXP_EINMALPASSWORT, REG_EXP_EMAIL, trimFormValues } from '@ap-ws/common-utils';
import { ChangeTempPasswordPayload } from '@auth-app/model';
import { Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from "@angular/material/dialog";
import { InfoDialogComponent, ZweiPassworteComponent } from '@ap-ws/common-ui';
import { ChangeTempPasswordFacade } from '@auth-app/change-temp-password/api';
import { ZweiPassworte } from '@ap-ws/common-model';

@Component({
  selector: 'auth-temp-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    ZweiPassworteComponent
  ],
  templateUrl: './change-temp-password.component.html',
  styleUrl: './change-temp-password.component.scss',
})
export class ChangeTempPasswordComponent implements OnInit, OnDestroy {

  changeTempPasswordFacade = inject(ChangeTempPasswordFacade)
  #formBuilder = inject(FormBuilder);
  #router = inject(Router);
  #activatedRoute = inject(ActivatedRoute);

  tokenId: string | null = null;
  #visibilityTimeout: any;
  showPassword = false;

  parentForm!: FormGroup;
  email!: AbstractControl;
  tempPassword!: AbstractControl;

  #zweiPassworte: ZweiPassworte = {
    passwort: '',
    passwortWdh: ''
  };
  #neuePasswoerterValid = false;

  #subscriptions: Subscription = new Subscription();

  constructor(public confirmDeleteDialog: MatDialog) {
    this.#createForm();
  }

  ngOnInit(): void {

    this.tokenId = this.#activatedRoute.snapshot.queryParamMap.get('tokenId');

    if (this.tokenId === null) {
      this.#showDialog('Fehler!', 'Der aufgerufene Link ist ungültig. Bitte kopieren Sie den Link vollständig oder klicken Sie ihn nochmals an.');
    }

    // const submittingSubscription = this.changeTempPasswordFacade.submittingForm$.subscribe((submitting) => this.#submittingForm = submitting);
    const tempPasswordSuccessMessageSubscription = this.changeTempPasswordFacade.tempPasswordSuccessMessage$.subscribe((message) => {
      if (message) {
        this.#showDialog('Passwort erfolgreich geändert', message);
      }
    });

    // this.#subscriptions.add(submittingSubscription);
    this.#subscriptions.add(tempPasswordSuccessMessageSubscription);
  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
    this.changeTempPasswordFacade.onDestroyComponent();
  }

  buttonSubmitDisabled(): boolean {
    if (!this.#neuePasswoerterValid || !this.parentForm.valid) {
      return true;
    }
    return false;
  }

  handlePasswordChanges(zweiPassworte: ZweiPassworte): void {
    console.log(JSON.stringify(zweiPassworte));    
    this.#zweiPassworte = zweiPassworte;
  }

  handleNeuePasswoerterValidityChanges(isValid: boolean): void {
    this.#neuePasswoerterValid = isValid;
  }

  togglePasswordVisibility(): void {

    if (!this.showPassword) {
      this.showPassword = true;
      this.#clearVisibilityTimeout();
      this.#visibilityTimeout = setTimeout(() => {
        this.showPassword = false;
      }, 5000);
    } else {
      this.showPassword = false;
      this.#clearVisibilityTimeout();
    }
  }

  submit(): void {

    if (this.parentForm.valid) {
      const payload = this.#trimAndReadFormValues();
      this.changeTempPasswordFacade.changeTempPassword(payload);
    }
  }

  gotoStartseite() {
    this.#router.navigateByUrl('/home');
  }

  gotoPasswortVergessen() {
    this.#router.navigateByUrl('/password/temp/order');
  }

  #showDialog(title: string, message: string) {

    const dialogRef = this.confirmDeleteDialog.open(InfoDialogComponent, {
      width: '500px',
      disableClose: true,
      data: {
        title: title,
        question: message
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.gotoStartseite();
      }
    });
  }

  #createForm() {
    this.parentForm = this.#formBuilder.group({
      email: ['', [Validators.required, Validators.maxLength(255), Validators.pattern(REG_EXP_EMAIL)]],
      tempPassword: ['', [Validators.required, Validators.maxLength(36), Validators.pattern(REG_EXP_EINMALPASSWORT)]]
      
    });

    this.email = this.parentForm.controls['email'];
    this.tempPassword = this.parentForm.controls['tempPassword'];
  }

  #trimAndReadFormValues(): ChangeTempPasswordPayload {
    trimFormValues(this.parentForm);

    const parentFormValues = this.parentForm.value;

    const result: ChangeTempPasswordPayload = {
      email: parentFormValues['email'],
      tempPassword: parentFormValues['tempPassword'],
      tokenId: this.tokenId === null ? 'noop' : this.tokenId,
      zweiPassworte: this.#zweiPassworte
    }

    console.log('auslesen form: ' + JSON.stringify(result));

    return result;
  }

  #clearVisibilityTimeout() {
    if (this.#visibilityTimeout) {
      clearTimeout(this.#visibilityTimeout);
      this.#visibilityTimeout = null;
    }
  }  
}

