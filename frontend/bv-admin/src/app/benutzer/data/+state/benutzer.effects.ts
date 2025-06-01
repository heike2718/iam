import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { BenutzerHttpService } from "../benutzer-http.service";
import { benutzerActions } from './benutzer.actions';
import { map, switchMap, tap } from "rxjs";
import { MessageService } from "@bv-admin/shared/messages/api";


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
            tap(() => {
                this.#messageService.info('Benutzer erfolgreich gelöscht');
            }),
        ), { dispatch: false });

    updateBenutzerflags$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(benutzerActions.uPDATE_BENUTZER_FLAGS),
            switchMap((action) => this.#httpService.updateBenutzerFlags(action.uuid, action.flagsDto)),
            map((result) => benutzerActions.bENUTZERFLAGS_UPDATED({ result }))
        );
    });

    benutzerFlagsUpdated$ = createEffect(() =>

        this.#actions.pipe(
            ofType(benutzerActions.bENUTZERFLAGS_UPDATED),
            tap((action) => {
                if (action.result.benuzer) {
                    this.#messageService.info('Benutzer erfolgreich geändert');
                } else {
                    this.#messageService.warn('Upsi, der Benutzer hat sein Konto in der Zwischenzeit anscheinend gelöscht');
                }
            }),
        ), { dispatch: false });
}