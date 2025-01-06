import { CommonModule } from "@angular/common";
import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { AbstractControl, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDialog } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { ActivatedRoute, Params } from "@angular/router";
import { ZweiPassworte } from "@ap-ws/common-model";
import { InfoDialogComponent, ZweiPassworteComponent } from "@ap-ws/common-ui";
import { NAME_REGELN, PASSWORT_LOGIN_ERLAUBTE_ZEICHEN, REG_EXP_INPUT_SECURED, REG_EXP_LOGIN_NAME, SIGNUP_SUCCESS_DIALOG_TEXT, trimFormValues } from "@ap-ws/common-utils";
import { LoginSignupFacade } from "@auth-app/login-signup/api";
import { SignUpFormModel, SignUpCredentials } from "@auth-app/login-signup/model";
import { ClientCredentials, ClientInformation } from "@auth-app/model";
import { Subscription } from "rxjs";
import { AuthAppConfiguration } from "../../../config/auth-app.configuration";



@Component({
  selector: 'auth-sign-up',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    ZweiPassworteComponent
  ],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss',
})
export class SignUpComponent implements OnInit, OnDestroy {


  #loginSignupFacade = inject(LoginSignupFacade);
  #configuration = inject(AuthAppConfiguration);

  benutzerForm!: FormGroup;

  validationErrorNamen: string = NAME_REGELN;
  passwortErlaubteZeichen = PASSWORT_LOGIN_ERLAUBTE_ZEICHEN;

  showLoginNameControl = false;
  showVornameNachnameControl = false;

  #route = inject(ActivatedRoute);

  #subscriptions: Subscription = new Subscription();

  #nonce: string | undefined;
  #passwoerterValid = false;
  #zweiPassworte: ZweiPassworte = { passwort: '', passwortWdh: '' };

  #clientCredentials!: ClientCredentials;

  #fb: FormBuilder = new FormBuilder();

  constructor(public infoBenutzerkontoDialog: MatDialog) {
  }

  ngOnInit(): void {

    this.#createForm();

    const routeSubscription = this.#route.queryParams.subscribe(
      params => this.#loadClientInfo(params)
    );

    const clientCredentialsSubscription = this.#loginSignupFacade.clientCredentials$.subscribe((cc) => this.#clientCredentials = cc);

    const clientInformationSubscription = this.#loginSignupFacade.clientInformation$.subscribe((clientInformation) => {
      if (clientInformation) {
        this.#updateForm(clientInformation);
      }
    });

    const redirectSubscription = this.#loginSignupFacade.redirectUrl$.subscribe((redirectUrl) => {
      if (redirectUrl) {
        this.#showSuccessDialogAndRedirect(redirectUrl);
      }
    })

    this.#subscriptions.add(routeSubscription);
    this.#subscriptions.add(clientCredentialsSubscription);
    this.#subscriptions.add(clientInformationSubscription);
    this.#subscriptions.add(redirectSubscription);
  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
  }

  buttonSubmitDisabled(): boolean {
    if (this.benutzerForm.invalid && this.#passwoerterValid && this.#clientCredentials) {
      return true;
    }
    return false;
  }


  handlePasswordChanges(zweiPassworte: ZweiPassworte): void {
    this.#zweiPassworte = zweiPassworte;
  }

  handlePasswoerterValidityChanges(isValid: boolean): void {
    this.#passwoerterValid = isValid;
  }


  submit(): void {

    const formValues: SignUpFormModel = this.#trimAndReadFormValues();

    const signUpCredentials: SignUpCredentials = {
      agbGelesen: formValues.agbGelesen,
      clientCredentials: this.#clientCredentials,
      email: formValues.email,
      kleber: formValues.kleber,
      loginName: formValues.loginName,
      nachname: formValues.nachname,
      zweiPassworte: this.#zweiPassworte,
      vorname: formValues.vorname,
      nonce: this.#nonce
    }

    this.#loginSignupFacade.signUp(signUpCredentials);
  }

  #showSuccessDialogAndRedirect(redirectUrl: string) {

    const dialogRef = this.infoBenutzerkontoDialog.open(InfoDialogComponent, {
      width: '500px',
      disableClose: true,
      data: {
        title: 'Benutzerkonto angelegt',
        text: SIGNUP_SUCCESS_DIALOG_TEXT
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (!this.#configuration.production) {
          console.log('redirect to ' + redirectUrl);
        }
        this.#loginSignupFacade.resetState();
        window.location.href = redirectUrl;
      }
    });
  }

  #loadClientInfo(params: Params) {

    // https://mathe-jung-alt.de/auth-app#/signup?accessToken=410b0d049290454a99f1cdfe82876862&state=signup&nonce=LEHRER-OXEO0PY0&redirectUrl=https:%2F%2Fmathe-jung-alt.de%2Fmkv-app
    // https://mathe-jung-alt.de/auth-app#/signup?accessToken=9566d324574b4750a7a7ff06e6916712&state=signup&redirectUrl=https:%2F%2Fmathe-jung-alt.de%2Fmja-app%2F

    const accessToken = params['accessToken'];
    const redirectUrl = params['redirectUrl'];
    const state = params['state'];
    this.#nonce = params['nonce'];

    if (accessToken && redirectUrl && state) {
      const cc: ClientCredentials = {
        accessToken: accessToken,
        redirectUrl: redirectUrl,
        state: state
      };

      this.#loginSignupFacade.loadClientCredentials(cc);

    }
  }

  #createForm(): void {
    this.benutzerForm = this.#fb.group({
      agbGelesen: [false, [Validators.required]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
      kleber: ['']
    });
  }

  #updateForm(clientInformation: ClientInformation): void {
    if (clientInformation) {
      if (clientInformation.loginnameSupported) {
        this.showLoginNameControl = true;

        const loginNameControl = this.#fb.control('', [Validators.pattern(REG_EXP_LOGIN_NAME), Validators.maxLength(255)]);

        this.benutzerForm.addControl(
          'loginName',
          loginNameControl
        );
      }

      if (clientInformation.namenRequired) {
        this.showVornameNachnameControl = true;

        const vornameControl = this.#fb.control('', [Validators.required, Validators.maxLength(100), Validators.pattern(REG_EXP_INPUT_SECURED)]);
        const nachnameControl = this.#fb.control('', [Validators.required, Validators.maxLength(100), Validators.pattern(REG_EXP_INPUT_SECURED)]);

        this.benutzerForm.addControl(
          'vorname',
          vornameControl
        );

        this.benutzerForm.addControl(
          'nachname',
          nachnameControl
        );
      }
    }
  }


  #trimAndReadFormValues(): SignUpFormModel {
    trimFormValues(this.benutzerForm);
    return this.benutzerForm.value;
  }
}