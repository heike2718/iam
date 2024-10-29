import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Benutzerdaten } from "@profil-app/benutzerdaten/model";
import { map, switchMap, tap } from "rxjs";
import { BenutzerdatenHttpService } from "../benutzerdaten-http.service";
import { benutzerdatenActions } from "./benutzerdaten.actions";
import { MessageService } from "@ap-ws/messages/api";


@Injectable({
    providedIn: 'root'
})
export class BenutzerdatenEffects {

    #actions = inject(Actions);
    #httpDervice = inject(BenutzerdatenHttpService);
    #messageService = inject(MessageService);

    loadBenutzerdaten$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(benutzerdatenActions.lOAD_BENUTZERDATEN),
            switchMap(() => this.#httpDervice.loadBenutzerdaten()),
            map((benutzerdaten: Benutzerdaten) => benutzerdatenActions.bENUTZERDATEN_LOADED({ benutzerdaten }))
        );
    });

    benutzerdatenAendern$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(benutzerdatenActions.bENUTZERDATEN_AENDERN),
            switchMap((action) => this.#httpDervice.updateBenutzerdaten(action.benutzerdaten)),
            map((benutzerdaten: Benutzerdaten) => benutzerdatenActions.bENUTZERDATEN_GEAENDERT({ benutzerdaten }))
        );
    });

    benutzerdatenGeaendert$ = createEffect(() =>
        this.#actions.pipe(
            ofType(benutzerdatenActions.bENUTZERDATEN_GEAENDERT),
            tap(() => this.#messageService.info('Ihre Benutzerdaten wurden erfolgreich geändert.'))
        ), { dispatch: false })

}