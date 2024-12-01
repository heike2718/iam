import { inject, Injectable } from "@angular/core";
import { TempPasswordCredentials } from "@auth-app/model";
import { Store } from "@ngrx/store";
import { forgotPasswordActions, fromForgotPassword } from '@auth-app/forgot-password/data';
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ForgotPasswordFacade {

    #store = inject(Store);

    submittingForm$: Observable<boolean> = this.#store.select(fromForgotPassword.submittingForm);
    tempPasswordSuccess$: Observable<boolean> = this.#store.select(fromForgotPassword.tempPasswordSuccess);

    public orderTempPassword(tempPasswordCredentials: TempPasswordCredentials): void {

        this.#store.dispatch(forgotPasswordActions.oRDER_TEMP_PASSWORD({tempPasswordCredentials}));
    }

    public onDestroyComponent(): void {

        this.#store.dispatch(forgotPasswordActions.rESET_ORDER_TEMP_PASSWORD_STATE());
    }
}
