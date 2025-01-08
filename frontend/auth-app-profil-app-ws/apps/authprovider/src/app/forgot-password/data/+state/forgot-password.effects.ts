import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { ForgotPasswordHttpService } from "../forgot-password-http.service";
import { forgotPasswordActions } from "./forgot-password.actions";
import { map, switchMap } from "rxjs";
import { TempPasswordResponseDto } from "@authprovider/model";


@Injectable({
    providedIn: 'root'
})
export class ForgotPasswordEffects {

    #actions = inject(Actions);
    #httpService = inject(ForgotPasswordHttpService);

    loadBenutzerdaten$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(forgotPasswordActions.oRDER_TEMP_PASSWORD),
            switchMap((action) => this.#httpService.orderTempPassword(action.tempPasswordCredentials)),
            map((payload: TempPasswordResponseDto) => forgotPasswordActions.tEMP_PASSWORD_SUCCESS({ payload }))
        );
    });
}