import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { InfomailsHttpService } from '../infomails-http.service';
import { MessageService } from "@bv-admin-app/shared/messages/api";
import { infomailsActions } from "./infomails.actions";
import { map, switchMap, tap } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class InfomailsEffects {

    #actions = inject(Actions);
    #httpService = inject(InfomailsHttpService);
    #messageService = inject(MessageService);

    loadInfomailTexte$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(infomailsActions.lOAD_INFOMAILS),
            switchMap((action) => this.#httpService.loadInfomailTexte()),
            map((infomails) => infomailsActions.iNFOMAILS_LOADED({ infomails }))
        );
    });

    addInfomailText$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(infomailsActions.aDD_INFOMAIL),
            switchMap((action) => this.#httpService.insertInfomailtext(action.infomailRequestDto)),
            map((infomail) => infomailsActions.iNFOMAIL_ADDED({ infomail }))
        );
    });

    updateInfomailText$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(infomailsActions.uPDATE_INFOMAIL),
            switchMap((action) => this.#httpService.updateInfomail(action.uuid, action.infomailRequestDto)),
            map((responsePayload) => infomailsActions.iNFOMAIL_CHANGED({ responsePayload }))
        );
    });

    infomailTextAdded$ = createEffect(() =>

        this.#actions.pipe(
            ofType(infomailsActions.iNFOMAIL_ADDED),
            tap((action) => {
                this.#messageService.info('Infomailtext erfolgreich gespeichert: uuid=' + action.infomail.uuid);
            }),
        ), { dispatch: false });

    infomailTextChanged$ = createEffect(() =>

        this.#actions.pipe(
            ofType(infomailsActions.iNFOMAIL_CHANGED),
            tap((action) => {
                if (action.responsePayload.infomail) {
                    this.#messageService.info('Infomailtext erfolgreich gespeichert');
                } else {
                    this.#messageService.error('Speichern fehlgeschlagen. Infomailtext existiert nicht oder nicht mehr. Guggstu ins server.log.')
                }

            }),
        ), { dispatch: false });

}