import { inject, Injectable } from "@angular/core";
import { MessageService } from "@ap-ws/messages/api";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { ForgotPasswordHttpService } from "../forgot-password-http.service";
import { forgotPasswordActions } from "./forgot-password.actions";
import { map, switchMap, tap } from "rxjs";
import { TempPasswordResponseDto } from "@auth-app/model";


@Injectable({
    providedIn: 'root'
})
export class ForgotPasswordEffects {

    #actions = inject(Actions);
    #httpService = inject(ForgotPasswordHttpService);
    #messageService = inject(MessageService);

    loadBenutzerdaten$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(forgotPasswordActions.oRDER_TEMP_PASSWORD),
            switchMap((action) => this.#httpService.orderTempPassword(action.tempPasswordCredentials)),
            map((payload: TempPasswordResponseDto) => forgotPasswordActions.tEMP_PASSWORD_SUCCESS({ payload }))
        );
    });

    tempPasswordSuccess$ = createEffect(() =>
        this.#actions.pipe(
            ofType(forgotPasswordActions.tEMP_PASSWORD_SUCCESS),
            tap((payload) => {
                this.#messageService.info(payload.payload.message, false, 10000)

            })
        ), { dispatch: false })

}