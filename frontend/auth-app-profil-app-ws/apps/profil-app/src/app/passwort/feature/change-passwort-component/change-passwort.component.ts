import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { AuthFacade } from "@profil-app/auth/api";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { initialPasswortPayload, PasswortPayload, ZweiPassworte } from "@ap-ws/common-model";
import { ZweiPassworteComponent } from "@ap-ws/common-ui";
import { forbiddenPasswordValidator, PASSWORT_ERLAUBTE_ZEICHEN, REG_EXP_PASSWORD } from '@ap-ws/common-utils';
import { MatIconModule } from "@angular/material/icon";
import { PasswortFacade } from "@profil-app/passwort/api";
import { Subscription } from "rxjs";



@Component({
  selector: 'profil-passwort',
  templateUrl: './change-passwort.component.html',
  styleUrl: './change-passwort.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    AsyncPipe,
    ZweiPassworteComponent
  ]
})
export class ChangePasswortComponent implements OnInit, OnDestroy {

  authFacade = inject(AuthFacade);
  passwortFacade = inject(PasswortFacade);

  parentForm!: FormGroup;

  passwortErlaubteZeichen = PASSWORT_ERLAUBTE_ZEICHEN;

  #neuePasswoerterValid = false;

  #fb = new FormBuilder();
  #subscriptions = new Subscription();
  #passwort: PasswortPayload = initialPasswortPayload;

  #visibilityTimeout: any;
  showPassword = false;

  ngOnInit(): void {
    this.parentForm = this.#fb.group({
      passwort: ['', {
        validators: [Validators.required, forbiddenPasswordValidator(REG_EXP_PASSWORD)]
      }]
    });

    const passwortSubscription = this.passwortFacade.passwort$.subscribe((passwort) => this.#passwort = {...passwort});
    this.#subscriptions.add(passwortSubscription);
  }

  ngOnDestroy(): void {
    this.#clearVisibilityTimeout();
    this.#subscriptions.unsubscribe();
    this.passwortFacade.passwortWegraeumen();
  }

  buttonSubmitDisabled(): boolean {
    if (!this.#neuePasswoerterValid || !this.parentForm.valid) {
      return true;
    }
    return false;
  }

  buttonCancelDisabled(): boolean {
    return false;
  }

  handlePasswordChanges(zweiPassworte: ZweiPassworte): void {    
    this.#passwort = {
      ...this.#passwort,
      zweiPassworte: zweiPassworte
    }
    console.log(JSON.stringify(this.#passwort));
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

  save(): void {

    const passwordValue = this.parentForm.get('passwort')?.value || '';

    if (this.parentForm.valid && passwordValue.length > 0) {
      const passwortPayload: PasswortPayload = {
        ...this.#passwort,
        passwort: passwordValue
      };

      this.passwortFacade.passwortAendern(passwortPayload);
    }
  }

  #clearVisibilityTimeout() {
    if (this.#visibilityTimeout) {
      clearTimeout(this.#visibilityTimeout);
      this.#visibilityTimeout = null;
    }
  }

}