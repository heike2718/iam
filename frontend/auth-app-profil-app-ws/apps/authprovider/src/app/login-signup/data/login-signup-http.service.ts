import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { ClientCredentials, ClientInformation, LoginCredentials, SignUpLogInResponseData } from "@authprovider/model";
import { SignUpCredentials } from "@authprovider/login-signup/model";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class LoginSignupHttpService {

    #httpClient = inject(HttpClient);

    loadClientInformation(clientCredentials: ClientCredentials): Observable<ClientInformation> {

        const url = '/authprovider/api/clients/v2/' + '?accessToken=' + clientCredentials.accessToken + '&redirectUrl=' + clientCredentials.redirectUrl + '&state=' + '';
        return this.#httpClient.get<ClientInformation>(url);

    }

    signUp(signUpCredentials: SignUpCredentials): Observable<SignUpLogInResponseData> {

        const url = '/authprovider/api/users/signup';

        return this.#httpClient.post<SignUpLogInResponseData>(url, signUpCredentials);
        
    }

    logIn(logInCredentials: LoginCredentials): Observable<SignUpLogInResponseData> {

        const url = '/authprovider//api/auth/sessions/auth-token-grant';

        return this.#httpClient.post<SignUpLogInResponseData>(url, logInCredentials);
    }

}