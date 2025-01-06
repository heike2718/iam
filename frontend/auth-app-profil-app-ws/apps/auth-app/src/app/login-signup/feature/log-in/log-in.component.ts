import { CommonModule } from "@angular/common";
import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { PASSWORT_LOGIN_ERLAUBTE_ZEICHEN, REG_EXP_LOGIN_NAME, trimFormValues } from "@ap-ws/common-utils";
import { LoginSignupFacade } from "@auth-app/login-signup/api";
import { AuthorizationCredentials, ClientCredentials, LoginCredentials } from "@auth-app/model";
import { Subscription } from "rxjs";
import { AuthAppConfiguration } from "../../../config/auth-app.configuration";


@Component({
    selector: 'auth-log-in',
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
    templateUrl: './log-in.component.html',
    styleUrl: './log-in.component.scss',
})
export class LoginComponent implements OnInit, OnDestroy {

    loginForm!: FormGroup;

    headline = 'Einloggen';

    passwortErlaubteZeichen = PASSWORT_LOGIN_ERLAUBTE_ZEICHEN;

    showLoginNameControl = false;
    showVornameNachnameControl = false;

    #loginSignupFacade = inject(LoginSignupFacade);
    #activatedRoute = inject(ActivatedRoute);
    #router = inject(Router);
    #configuration = inject(AuthAppConfiguration);

    #clientCredentials!: ClientCredentials;

    #visibilityTimeout: any;
    showPassword = false;

    #fb: FormBuilder = new FormBuilder();

    #subscriptions: Subscription = new Subscription();

    ngOnInit(): void {

        this.#createForm();

        const routeSubscription = this.#activatedRoute.queryParams.subscribe(
            params => this.#loadClientInfo(params)
        );

        const clientCredentialsSubscription = this.#loginSignupFacade.clientCredentials$.subscribe((cc) => this.#clientCredentials = cc);

        const clientInformationSubscription = this.#loginSignupFacade.clientInformation$.subscribe(
            (clientInfo) => {
                if (clientInfo) {
                    this.headline = 'Einloggen ' + clientInfo.zurueckText;
                }
            }
        )

        const redirectSubscription = this.#loginSignupFacade.redirectUrl$.subscribe((redirectUrl) => {
            if (redirectUrl) {
                if (!this.#configuration.production) {
                    console.log('redirect to ' + redirectUrl);
                }
                this.#loginSignupFacade.resetState();
                window.location.href = redirectUrl;
            }
        });

        this.#subscriptions.add(routeSubscription);
        this.#subscriptions.add(clientCredentialsSubscription);
        this.#subscriptions.add(clientInformationSubscription);
        this.#subscriptions.add(redirectSubscription);
    }

    ngOnDestroy(): void {
        this.#subscriptions.unsubscribe();
    }

    buttonSubmitDisabled(): boolean {
        if (this.loginForm.invalid && this.#clientCredentials) {
            return true;
        }
        return false;
    }

    submit(): void {

        const authCredentials = this.#trimAndReadFormValues();

        const loginCredentials: LoginCredentials = {
            clientCredentials: this.#clientCredentials,
            authorizationCredentials: authCredentials,
        }

        this.#loginSignupFacade.logIn(loginCredentials);
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

    gotoOrderTempPassword() {
        this.#router.navigateByUrl('password/temp/order');
    }

    #createForm(): void {
        this.loginForm = this.#fb.group({
            'loginName': ['', [Validators.required, Validators.maxLength(255), Validators.pattern(REG_EXP_LOGIN_NAME)]],
            'passwort': ['', [Validators.required]],
            'kleber': ['']
        });
    }

    #loadClientInfo(params: Params) {


        // https://mathe-jung-alt.de/auth-app#/login?accessToken=8bd728570cfb4960a9dc5ab8c4766155&state=login&redirectUrl=https:%2F%2Fmathe-jung-alt.de%2Fmja-app%2F

        const accessToken = params['accessToken'];
        const redirectUrl = params['redirectUrl'];
        const state = params['state'];

        if (accessToken && redirectUrl && state) {
            const cc: ClientCredentials = {
                accessToken: accessToken,
                redirectUrl: redirectUrl,
                state: state
            };

            this.#loginSignupFacade.loadClientCredentials(cc);

        }
    }



    #clearVisibilityTimeout() {
        if (this.#visibilityTimeout) {
            clearTimeout(this.#visibilityTimeout);
            this.#visibilityTimeout = null;
        }
    }

    #trimAndReadFormValues(): AuthorizationCredentials {
        trimFormValues(this.loginForm);
        return this.loginForm.value;
    }

}