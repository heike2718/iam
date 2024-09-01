import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { VersandauftraegeHttpService } from "../versandauftraege-http.service";
import { versandauftraegeActions } from "./versandauftraege.actions";
import { map, switchMap, tap } from "rxjs";
import { MessageService } from "@bv-admin-app/shared/messages/api";
import { Router } from "@angular/router";
import { MailversandgruppeDetailsResponseDto } from "@bv-admin-app/versandauftraege/model";

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
            switchMap((action) => this.#httpService.loadVersandauftragDetails(action.uuid)),
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

    scheduleVersandauftrag$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.sCHEDULE_VERSANDAUFTRAG),
            switchMap((action) => this.#httpService.scheduleVersandauftrag(action.requestDto)),
            map((versandauftrag) => versandauftraegeActions.vERSANDAUFTRAG_SCHEDULED({ versandauftrag }))
        );
    });

    cancelVersandauftrag$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.cANCEL_VERSANDAUFTRAG),
            switchMap((action) => this.#httpService.cancelVersandauftrag(action.uuid)),
            map((responsePayload) => versandauftraegeActions.lOAD_VERSANDAUFTRAG_DETAILS({ uuid: responsePayload.uuid }))
        );
    });

    continueVersandauftrag$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.cONTINUE_VERSANDAUFTRAG),
            switchMap((action) => this.#httpService.continueVersandauftrag(action.uuid)),
            map((responsePayload) => versandauftraegeActions.lOAD_VERSANDAUFTRAG_DETAILS({ uuid: responsePayload.uuid }))
        );
    });

    deleteVersandauftrag$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.dELETE_VERSANDAUFTRAG),
            switchMap((action) => this.#httpService.deleteVersandauftrag(action.uuid)),
            map((responsePayload) => versandauftraegeActions.vERSANDAUFTRAG_DELETED({ responsePayload }))
        );
    });

    versandauftragScheduled$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.vERSANDAUFTRAG_SCHEDULED),
            tap((action) => {
                this.#messageService.info('Versandauftrag erfolgreich gespeichert: uuid=' + action.versandauftrag.uuid);
            }),
        ), { dispatch: false });

    loadVersandgruppeDetails$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.lOAD_VERSANDGRUPPE),
            switchMap((action) => this.#httpService.loadMailversandgruppedetails(action.uuid)),
            map((responsePayload) => versandauftraegeActions.vERSANDGRUPPE_LOADED({ responsePayload }))
        );
    });

    saveVersandgruppe$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(versandauftraegeActions.vERSANDGRUPPE_SPEICHERN),
            switchMap((action) => this.#httpService.updateMailversandgruppe(action.mailversandgruppe)),
            map((responsePayload) => versandauftraegeActions.vERSANDGRUPPE_LOADED({ responsePayload }))
        );
    });

    versandgruppeDetailsLoaded$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.vERSANDGRUPPE_LOADED),
            tap((action) => {
                if (action.responsePayload.mailversandgruppe) {
                    this.#router.navigateByUrl('/versandauftraege/gruppe');
                }
            })
        ), { dispatch: false });

    unselectVersandauftrag$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.uNSELECT_VERSANDAUFTRAG),
            tap(() => {
                this.#router.navigateByUrl('/versandauftraege');
            })
        ), { dispatch: false });

    unselectVersandgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(versandauftraegeActions.uNSELECT_VERSANDGRUPPE),
            tap(() => {
                this.#router.navigateByUrl('/versandauftraege/details');
            })
        ), { dispatch: false });

}