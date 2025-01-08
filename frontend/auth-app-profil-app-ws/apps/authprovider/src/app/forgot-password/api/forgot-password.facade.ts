import { inject, Injectable } from "@angular/core";
import { TempPasswordCredentials } from "@authprovider/model";
import { Store } from "@ngrx/store";
import { forgotPasswordActions, fromForgotPassword } from '@authprovider/forgot-password/data';
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ForgotPasswordFacade {

    #store = inject(Store);

    submittingForm$: Observable<boolean> = this.#store.select(fromForgotPassword.submittingForm);
    tempPasswordSuccess$: Observable<boolean> = this.#store.select(fromForgotPassword.tempPasswordSuccess);
    tempPasswordSuccessMessage$: Observable<string | undefined> = this.#store.select(fromForgotPassword.tempPasswordSuccessMessage);

    public orderTempPassword(tempPasswordCredentials: TempPasswordCredentials): void {

        this.#store.dispatch(forgotPasswordActions.oRDER_TEMP_PASSWORD({tempPasswordCredentials}));
    }

    public onDestroyComponent(): void {

        this.#store.dispatch(forgotPasswordActions.rESET_ORDER_TEMP_PASSWORD_STATE());
    }
}
