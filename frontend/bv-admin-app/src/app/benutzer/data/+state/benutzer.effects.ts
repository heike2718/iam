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
    #benutzerHttpService = inject(BenutzerHttpService);
    #messageService = inject(MessageService);

    findBenutzer$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(benutzerActions.fIND_BENUTZER),
            switchMap((action) => this.#benutzerHttpService.findBenutzer(action.suchparameter)),
            map((treffer) => benutzerActions.bENUTZER_FOUND({ treffer }))
        );
    });

    deleteSingleBenutzer$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(benutzerActions.dELETE_SINGLE_BENUTZER),
            switchMap((action) => this.#benutzerHttpService.deleteBenutzer(action.benutzer.uuid)),
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


}