import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { VersandauftraegeHttpService } from "../versandauftraege-http.service";
import { versandauftraegeActions } from "./versandauftraege.actions";
import { map, switchMap, tap } from "rxjs";
import { MessageService } from "@bv-admin-app/shared/messages/api";
import { Router } from "@angular/router";

@Injectable(
    {
        providedIn: 'root'
    }
)
export class VersandauftraegeEffects {

    #actions = inject(Actions);
    #httpService = inject(VersandauftraegeHttpService);
    #messageService = inject(MessageService);
    #router = inject(Router);

    loadVersandauftraege$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.lOAD_VERSANDAUFTRAEGE),
            switchMap(() => this.#httpService.loadVersandauftraege()),
            map((versandauftraege) => versandauftraegeActions.vERSANDAUFTRAEGE_LOADED({ versandauftraege }))
        );
    });

    loadVersandauftragDetails$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.lOAD_VERSANDAUFTRAG_DETAILS),
            switchMap((action) => this.#httpService.loadDetails(action.uuid)),
            map((responsePayload) => versandauftraegeActions.vERSANDAUFTRAG_DETAILS_LOADED({ responsePayload }))
        );
    });

    versandauftragDetailsLoadad$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.vERSANDAUFTRAG_DETAILS_LOADED),
            tap((action) => {
                if (!action.responsePayload.versandauftrag) {
                    this.#router.navigateByUrl('/versandauftraege');
                } else {
                    this.#router.navigateByUrl('/versandauftraege/details');
                }
            }),
        ), { dispatch: false });

    selectVersandauftrag$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.sELECT_VERSANDAUFTRAG),
            tap(() => {
                this.#router.navigateByUrl('/versandauftraege/details');
            }),
        ), { dispatch: false });

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

    versandgruppeSelected$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.vERSANDGRUPPE_SELECTED),
            tap(() => {
                this.#router.navigateByUrl('/versandauftraege/gruppe');
            }),
        ), { dispatch: false });

}