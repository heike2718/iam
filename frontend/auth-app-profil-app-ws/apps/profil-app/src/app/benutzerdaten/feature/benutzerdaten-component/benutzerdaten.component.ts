import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { AuthFacade } from "@profil-app/auth/api";
import { BenutzerdatenFacade } from "@profil-app/benutzerdaten/api";
import { anonymeBenutzerdaten, benutzerAreEqual, Benutzerdaten } from "@profil-app/benutzerdaten/model";
import { Subscription } from "rxjs";
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { trimFormValues } from "@ap-ws/common-utils";


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
    AsyncPipe
  ]
})
export class BenutzerdatenComponent implements OnInit, OnDestroy {

  authFacade = inject(AuthFacade);
  benutzerdatenFacade = inject(BenutzerdatenFacade);
  benutzerForm!: FormGroup;

  #subscriptions = new Subscription();
  #cachedBenutzerdaten: Benutzerdaten = anonymeBenutzerdaten;
  #fb: FormBuilder = new FormBuilder();

  ngOnInit(): void {

    this.benutzerForm = this.#fb.group({
      loginName: ['', [Validators.required, Validators.maxLength(255)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
      vorname: ['', [Validators.required, Validators.maxLength(100)]],
      nachname: ['', [Validators.required, Validators.maxLength(100)]],
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
    const updatedBenutzer: Benutzerdaten = this.#readFormValues();

    if (benutzerAreEqual(this.#cachedBenutzerdaten, updatedBenutzer)) {
      return true;
    }

    return false;
  }

  buttonCancelDisabled(): boolean {
    const updatedBenutzer: Benutzerdaten = this.#readFormValues();
    if (!benutzerAreEqual(this.#cachedBenutzerdaten, updatedBenutzer)) {
      return false;
    }

    return true;
  }

  save(): void {
    if (this.benutzerForm.valid) {
      const updatedBenutzer: Benutzerdaten = this.#readFormValues();
      this.benutzerdatenFacade.benutzerdatenAendern(updatedBenutzer);
    }
  }

  cancel(): void {
    // oder man läd sie aus der DB nach?
    this.#patchFormValues(this.#cachedBenutzerdaten);
  }

  #patchFormValues(benutzerdaten: Benutzerdaten): void {
    this.benutzerForm.patchValue(benutzerdaten);
  }

  #readFormValues(): Benutzerdaten {
    trimFormValues(this.benutzerForm);
    return this.benutzerForm.value;
  }
}