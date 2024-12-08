import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { changeTempPasswordActions } from "./change-temp-password.actions";
import { map, switchMap } from "rxjs";;
import { ChangeTempPasswordHttpService } from "../change-temp-password-http.service";
import { ResponsePayload } from "@ap-ws/common-model";


@Injectable({
    providedIn: 'root'
})
export class ChangeTempPasswordEffects {

    #actions = inject(Actions);
    #httpService = inject(ChangeTempPasswordHttpService);

    loadBenutzerdaten$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(changeTempPasswordActions.cHANGE_TEMP_PASSWORD),
            switchMap((action) => this.#httpService.changeTempPassword(action.changeTempPasswordPayload)),
            map((payload: ResponsePayload) => changeTempPasswordActions.tEMP_PASSWORD_CHANGED({ payload }))
        );
    });
}