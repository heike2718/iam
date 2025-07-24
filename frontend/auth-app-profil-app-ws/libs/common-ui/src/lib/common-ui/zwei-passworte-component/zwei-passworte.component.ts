import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';

import { ZweiPassworte } from '@ap-ws/common-model';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { debounceTime, distinctUntilChanged, min, Subscription } from 'rxjs';
import { forbiddenPasswordValidator, passwordsMatchValidator, PASSWORT_NEU_ERLAUBTE_ZEICHEN, PASSWORTREGELN, REG_EXP_PASSWORT_NEU } from '@ap-ws/common-utils';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'auth-common-zwei-passworte',
  standalone: true,
  imports: [
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule
],
  templateUrl: './zwei-passworte.component.html',
  styleUrl: './zwei-passworte.component.scss',
})
export class ZweiPassworteComponent implements OnInit, OnDestroy {

  #fb = new FormBuilder();

  @Input()
  labelPasswort1 = '';

  @Input()
  labelPasswort2 = '';

  @Output()
  passwordChanges = new EventEmitter<ZweiPassworte>();

  @Output()
  isValid = new EventEmitter<boolean>();

  passwortForm!: FormGroup;

  passwortregeln = PASSWORTREGELN;

  passwortErlaubteZeichen = PASSWORT_NEU_ERLAUBTE_ZEICHEN;

  showPasswords = false;
  
  #visibilityTimeout: any;
  #subsriptions = new Subscription();

  ngOnInit(): void {
    this.passwortForm = this.#fb.group(
      {
        passwortNeu: ['', {
          validators: [Validators.required, Validators.min(8), Validators.max(100), forbiddenPasswordValidator(REG_EXP_PASSWORT_NEU)]
        }],
        passwortNeuWdh: ['', {
          validators: [Validators.required]
        }]
      },
      { validators: passwordsMatchValidator(), updateOn: 'change' }
      // validations für die gesamte formGroup
    );

    const formSubscription = this.passwortForm.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe(() => {
        const formIsValid = this.passwortForm.valid;
        this.isValid.emit(formIsValid);

        if (formIsValid) {

          const thePasswort = this.passwortForm.get('passwortNeu')?.value || '';
          const thePasswortWdh = this.passwortForm.get('passwortNeuWdh')?.value || '';

          if (thePasswort.length > 0 &&thePasswortWdh.length > 0) {
            this.passwordChanges.emit({
              passwort: thePasswort,
              passwortWdh: thePasswortWdh
            })
          }
        }
      });

    this.#subsriptions.add(formSubscription);
  }

  ngOnDestroy(): void {
    this.#subsriptions.unsubscribe();
    this.#clearVisibilityTimeout();
  }

  togglePasswordsVisibility(): void {

    if (!this.showPasswords) {
      this.showPasswords = true;
      this.#clearVisibilityTimeout();

      this.#visibilityTimeout = setTimeout(() => {
        this.showPasswords = false;
      }, 5000);

    } else {
      this.showPasswords = false;
      this.#clearVisibilityTimeout();
    }
  }  

  #clearVisibilityTimeout() {
    if (this.#visibilityTimeout) {
      clearTimeout(this.#visibilityTimeout);
      this.#visibilityTimeout = null;
    }
  }
}
