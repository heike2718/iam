import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Benutzerdaten, ChangeBenutzerdatenResponseDto } from "@benutzerprofil/benutzerdaten/model";
import { map, switchMap, tap } from "rxjs";
import { BenutzerdatenHttpService } from "../benutzerdaten-http.service";
import { benutzerdatenActions } from "./benutzerdaten.actions";
import { MessageService } from "@ap-ws/messages/api";
import { MESSAGE_BENUTZERDATEN_SUCCESS, MESSAGE_BENUTZERDATEN_SUCCESS_WITH_LOGOUT } from "@ap-ws/common-utils";


@Injectable({
    providedIn: 'root'
})
export class BenutzerdatenEffects {

    #actions = inject(Actions);
    #httpService = inject(BenutzerdatenHttpService);
    #messageService = inject(MessageService);

    loadBenutzerdaten$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(benutzerdatenActions.lOAD_BENUTZERDATEN),
            switchMap(() => this.#httpService.loadBenutzerdaten()),
            map((benutzerdaten: Benutzerdaten) => benutzerdatenActions.bENUTZERDATEN_LOADED({ benutzerdaten }))
        );
    });

    benutzerdatenAendern$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(benutzerdatenActions.bENUTZERDATEN_AENDERN),
            switchMap((action) => this.#httpService.updateBenutzerdaten(action.benutzerdaten)),
            map((responseDto: ChangeBenutzerdatenResponseDto) => benutzerdatenActions.bENUTZERDATEN_GEAENDERT({ responseDto }))
        );
    });

    benutzerdatenGeaendert$ = createEffect(() =>
        this.#actions.pipe(
            ofType(benutzerdatenActions.bENUTZERDATEN_GEAENDERT),
            tap((responseDto) => {
                const isSecurityRelevant = responseDto.responseDto.securityEvent;
                if (isSecurityRelevant) {
                    this.#messageService.info(MESSAGE_BENUTZERDATEN_SUCCESS_WITH_LOGOUT, responseDto.responseDto.securityEvent, 5000)
                } else {
                    this.#messageService.info(MESSAGE_BENUTZERDATEN_SUCCESS, responseDto.responseDto.securityEvent, 3000)
                }

            })
        ), { dispatch: false })

    kontoLoeschen$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(benutzerdatenActions.kONTO_LOESCHEN),
            switchMap(() => this.#httpService.deleteBenutzer()),
            map((message) => benutzerdatenActions.kONTO_GELOESCHT({ message }))
        );
    });

    kontoGeloescht$ = createEffect(() =>
        this.#actions.pipe(
            ofType(benutzerdatenActions.kONTO_GELOESCHT),
            tap((actionPayload) => {
                // muss logout triggern, also isSecurityEvent = true
                this.#messageService.info(actionPayload.message.message + ' Sie werden jetzt ausgeloggt.', true, 3000)
            })
        ), { dispatch: false })

}