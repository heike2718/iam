import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { VersandauftraegeHttpService } from "../versandauftraege-http.service";
import { versandauftraegeActions } from "./versandauftraege.actions";
import { map, switchMap, tap } from "rxjs";
import { MessageService } from "@bv-admin-app/shared/messages/api";

@Injectable(
    {
        providedIn: 'root'
    }
)
export class VersandauftraegeEffects {

    #actions = inject(Actions);
    #httpService = inject(VersandauftraegeHttpService);
    #messageService = inject(MessageService);

    sheduleVersandauftrag$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.sCHEDULE_VERSANDAUFTRAG),
            switchMap((action) => this.#httpService.scheduleVersandauftrag(action.requestDto)),
            map((versandauftrag) => versandauftraegeActions.vERSANDAUFTRAG_SCHEDULED({ versandauftrag }))
        );
    });

    versandauftragScheduled$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.vERSANDAUFTRAG_SCHEDULED),
            tap((action) => {
                this.#messageService.info('Versandauftrag erfolgreich gespeichert: uuid=' + action.versandauftrag.uuid);
            }),
        ), { dispatch: false });

}