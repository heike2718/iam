import { inject, Injectable } from "@angular/core";
import { ClientCredentials, ClientInformation, LoginCredentials } from "@auth-app/model";
import { SignUpCredentials } from "@auth-app/login-signup/model";
import { Store } from "@ngrx/store";
import { fromLoginSignup, loginSignupActions } from "../data";
import { Observable } from "rxjs";
import { filterDefined } from "@ap-ws/common-utils";


@Injectable({
    providedIn: 'root'
})
export class LoginSignupFacade {

    #store = inject(Store);

    clientCredentials$: Observable<ClientCredentials> = filterDefined(this.#store.select(fromLoginSignup.clientCredentials));

    clientInformation$: Observable<ClientInformation | undefined> = this.#store.select(fromLoginSignup.clientInformation);

    redirectUrl$: Observable<string | undefined> = this.#store.select(fromLoginSignup.redirectUrl);

    loadClientCredentials(clientCredentials: ClientCredentials): void {
        this.#store.dispatch(loginSignupActions.lOAD_CLIENT_INFORMATION({ clientCredentials }));

    }

    logIn(loginCredentials: LoginCredentials): void {
        this.#store.dispatch(loginSignupActions.lOG_IN({loginCredentials}));
    }

    signUp(signUpCredentials: SignUpCredentials) {
        this.#store.dispatch(loginSignupActions.sIGN_UP({signUpCredentials}));
    }

    resetState(): void {
        this,this.#store.dispatch(loginSignupActions.rESET_LOGIN_SIGNUP_STATE());
    }

}