import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ZweiPassworte } from '@ap-ws/common-model';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { debounceTime, distinctUntilChanged, Subscription } from 'rxjs';
import { forbiddenPasswordValidator, REG_EXP_PASSWORD, PASSWORTREGELN, REG_EXP_PASSWORT_NEU } from '@ap-ws/common-utils';

@Component({
  selector: 'auth-common-zwei-passworte',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
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

  @Input()
  praefixRegeln = '';

  @Output()
  passwordChanges = new EventEmitter<ZweiPassworte>();

  passwortForm!: FormGroup;

  passwortregeln = PASSWORTREGELN;

  #subsriptions = new Subscription();

  ngOnInit(): void {
    this.passwortForm = this.#fb.group({
      passwortNeu: ['', [forbiddenPasswordValidator(REG_EXP_PASSWORT_NEU)]],
      passwortNeuWdh: ['', [Validators.required]]
    });

    const formSubscription = this.passwortForm.valueChanges
      .pipe(
        debounceTime(300), // Delay to reduce frequency
        distinctUntilChanged() // Only emit when values are different
      )
      .subscribe(() => {
        if (this.passwortForm.valid) {
          this.passwordChanges.emit(this.passwortForm.value);
        }
      });

    this.#subsriptions.add(formSubscription);
  }

  ngOnDestroy(): void {
    this.#subsriptions.unsubscribe();
  }
}
