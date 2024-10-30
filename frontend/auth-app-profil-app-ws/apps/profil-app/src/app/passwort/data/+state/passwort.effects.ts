import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { PasswortHttpService } from "../passwort-http.service";
import { MessageService } from "@ap-ws/messages/api";
import { passwortActions } from "./passwort.actions";
import { map, switchMap, tap } from "rxjs";
import { Message } from "@ap-ws/common-model";

@Injectable({
    providedIn: 'root'
})
export class PasswortEffects {

    #actions = inject(Actions);
    #httpService = inject(PasswortHttpService);
    #messageService = inject(MessageService);

    passwortAendern$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(passwortActions.pASSWORT_AENDERN),
            switchMap((action) => this.#httpService.changePasswort(action.passwortPayload)),
            map((message: Message) => passwortActions.pASSWORT_GEAENDERT({ message }))
        );
    });

    passwortGeaendert$ = createEffect(() =>
        this.#actions.pipe(
            ofType(passwortActions.pASSWORT_GEAENDERT),
            tap((payload) => this.#messageService.info(payload.message.message))
        ), { dispatch: false })

}