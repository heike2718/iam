import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { BenutzerdatenHttpService } from "../benutzerdaten-http.service";
import { benutzerdatenActions } from "./benutzerdaten.actions";
import { map, switchMap } from "rxjs";
import { Benutzerdaten } from "@profil-app/benutzerdaten/model";


@Injectable({
    providedIn: 'root'
})
export class BenutzerdatenEffects {

    #actions = inject(Actions);
    #httpDervice = inject(BenutzerdatenHttpService);

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
            map((benutzerdaten: Benutzerdaten) => benutzerdatenActions.bENUTZERDATEN_LOADED({ benutzerdaten }))
        );
    });

}