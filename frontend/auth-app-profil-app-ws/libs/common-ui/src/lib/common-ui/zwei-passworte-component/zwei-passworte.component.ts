import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ZweiPassworte } from '@ap-ws/common-model';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { debounceTime, distinctUntilChanged, min, Subscription } from 'rxjs';
import { forbiddenPasswordValidator, passwordsMatchValidator, PASSWORTREGELN, REG_EXP_PASSWORT_NEU } from '@ap-ws/common-utils';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'auth-common-zwei-passworte',
  standalone: true,
  imports: [
    CommonModule,
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
        console.log('valid? ' + formIsValid)
        this.isValid.emit(formIsValid);
        if (formIsValid) {
          this.passwordChanges.emit(this.passwortForm.value);
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
