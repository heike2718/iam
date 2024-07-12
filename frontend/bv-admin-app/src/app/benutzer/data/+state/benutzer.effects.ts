import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { BenutzerHttpService } from "../benutzer-http.service";
import { benutzerActions } from './benutzer.actions';
import { map, switchMap, tap } from "rxjs";
import { MessageService } from "@bv-admin-app/shared/messages/api";


@Injectable({
    providedIn: 'root'
})
export class BenutzerEffects {

    #actions = inject(Actions);
    #httpService = inject(BenutzerHttpService);
    #messageService = inject(MessageService);

    findBenutzer$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(benutzerActions.fIND_BENUTZER),
            switchMap((action) => this.#httpService.findBenutzer(action.suchparameter)),
            map((treffer) => benutzerActions.bENUTZER_FOUND({ treffer }))
        );
    });

    deleteSingleBenutzer$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(benutzerActions.dELETE_SINGLE_BENUTZER),
            switchMap((action) => this.#httpService.deleteBenutzer(action.benutzer.uuid)),
            map((responsePayload) => benutzerActions.sINGLE_BENUTZER_DELETED({ responsePayload }))
        );
    });

    singleBenutzerDeleted$ = createEffect(() =>

        this.#actions.pipe(
            ofType(benutzerActions.sINGLE_BENUTZER_DELETED),
            tap((action) => {
                this.#messageService.info('Benutzer erfolgreich gelöscht');
            }),
        ), { dispatch: false });

    updateAktivierungsstatus$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(benutzerActions.uPDATE_BENUTZER_ACTIVATION_STATE),
            switchMap((action) => this.#httpService.updateBenutzerStatus(action.uuid, action.aktivierungsstatus)),
            map((result) => benutzerActions.bENUTZER_ACTIVATION_STATE_UPDATED({ result }))
        );
    });

    benutzerActivationStateUpdated$ = createEffect(() =>

        this.#actions.pipe(
            ofType(benutzerActions.bENUTZER_ACTIVATION_STATE_UPDATED),
            tap((action) => {
                if (action.result.benuzer) {
                    this.#messageService.info('Benutzer erfolgreich geändert');
                } else {
                    this.#messageService.warn('Upsi, der Benutzer hat sein Konto in der Zwischenzeit anscheinend gelöscht');
                }
            }),
        ), { dispatch: false });
}