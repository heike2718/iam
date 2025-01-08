import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { LoginSignupHttpService } from "../login-signup-http.service";
import { loginSignupActions } from "./login-signup.actions";
import { map, switchMap } from "rxjs";
import { ClientInformation, SignUpLogInResponseData } from "@authprovider/model";


@Injectable({
    providedIn: 'root'
})
export class LoginSignupEffects {

    #actions = inject(Actions);
    #httpService = inject(LoginSignupHttpService);

    loadClientInformation$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(loginSignupActions.lOAD_CLIENT_INFORMATION),
            switchMap((action) => this.#httpService.loadClientInformation(action.clientCredentials)),
            map((clientInformation: ClientInformation) => loginSignupActions.cLIENT_INFORMATION_LOADED({ clientInformation }))
        );
    });


    signUp$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(loginSignupActions.sIGN_UP),
            switchMap((action) => this.#httpService.signUp(action.signUpCredentials)),
            map((signUpLogInResponseData: SignUpLogInResponseData) => loginSignupActions.lOG_IN_SIGN_UP_SUCCESS({ signUpLogInResponseData }))
        );
    });

    logIn$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(loginSignupActions.lOG_IN),
            switchMap((action) => this.#httpService.logIn(action.loginCredentials)),
            map((signUpLogInResponseData: SignUpLogInResponseData) => loginSignupActions.lOG_IN_SIGN_UP_SUCCESS({ signUpLogInResponseData }))
        )
    })
}