import { AsyncPipe, CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from '@angular/material/input';
import { Router } from "@angular/router";
import { LOGINNAME_REGELN, NAME_REGELN, REG_EXP_INPUT_SECURED, REG_EXP_LOGIN_NAME, trimFormValues } from "@ap-ws/common-utils";
import { AuthFacade } from "@profil-app/auth/api";
import { BenutzerdatenFacade } from "@profil-app/benutzerdaten/api";
import { anonymeBenutzerdaten, benutzerAreEqual, Benutzerdaten } from "@profil-app/benutzerdaten/model";
import { Subscription } from "rxjs";


@Component({
  selector: 'profil-benutzerdaten',
  templateUrl: './benutzerdaten.component.html',
  styleUrl: './benutzerdaten.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    AsyncPipe
  ]
})
export class BenutzerdatenComponent implements OnInit, OnDestroy {

  authFacade = inject(AuthFacade);
  benutzerdatenFacade = inject(BenutzerdatenFacade);
  benutzerForm!: FormGroup;

  validationErrorLoginName: string = LOGINNAME_REGELN;
  validationErrorNamen: string = NAME_REGELN;

  #subscriptions = new Subscription();
  #cachedBenutzerdaten: Benutzerdaten = anonymeBenutzerdaten;
  #fb: FormBuilder = new FormBuilder();
  #router = inject(Router);

  constructor(private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {

    this.benutzerForm = this.#fb.group({
      loginName: ['', [Validators.required, Validators.maxLength(255), Validators.pattern(REG_EXP_LOGIN_NAME)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
      vorname: ['', [Validators.required, Validators.maxLength(100), Validators.pattern(REG_EXP_INPUT_SECURED)]],
      nachname: ['', [Validators.required, Validators.maxLength(100), Validators.pattern(REG_EXP_INPUT_SECURED)]],
    });

    const benutzerSubscription = this.benutzerdatenFacade.benutzerdaten$.subscribe((benutzerdaten) => {
      this.#cachedBenutzerdaten = { ...benutzerdaten };
      this.#patchFormValues(benutzerdaten);
    });

    this.#subscriptions.add(benutzerSubscription);
  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
  }

  buttonSubmitDisabled(): boolean {
    if (this.benutzerForm.invalid) {
      return true;
    }
    const updatedBenutzer: Benutzerdaten = this.benutzerForm.value;

    if (benutzerAreEqual(this.#cachedBenutzerdaten, updatedBenutzer)) {
      return true;
    }

    return false;
  }

  buttonCancelDisabled(): boolean {
    const updatedBenutzer: Benutzerdaten = this.benutzerForm.value;
    if (!benutzerAreEqual(this.#cachedBenutzerdaten, updatedBenutzer)) {
      return false;
    }

    return true;
  }

  save(): void {
    if (this.benutzerForm.valid) {
      const updatedBenutzer: Benutzerdaten = this.#trimAndReadFormValues();
      this.benutzerdatenFacade.benutzerdatenAendern(updatedBenutzer);
    }
  }

  cancel(): void {
    this.#patchFormValues(this.#cachedBenutzerdaten);
    this.benutzerForm.markAsPristine();
    this.benutzerForm.markAsUntouched();
    this.benutzerForm.updateValueAndValidity();
    this.cdr.detectChanges();
  }

  gotoStartseite() {
    console.log('navigate home');
    this.#router.navigateByUrl('/');
  }

  #patchFormValues(benutzerdaten: Benutzerdaten): void {
    this.benutzerForm.setValue(benutzerdaten);
  }

  
  #trimAndReadFormValues(): Benutzerdaten {
    trimFormValues(this.benutzerForm);
    return this.benutzerForm.value;
  }
}