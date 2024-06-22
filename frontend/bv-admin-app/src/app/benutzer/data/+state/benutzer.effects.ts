import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { BenutzerHttpService } from "../benutzer-http.service";
import { benutzerActions } from './benutzer.actions';
import { map, switchMap } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class BenutzerEffects {

    #actions = inject(Actions);
    #benutzerHttpService = inject(BenutzerHttpService);

    findBenutzer$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(benutzerActions.fIND_BENUTZER),
            switchMap((action) => this.#benutzerHttpService.findBenutzer(action.suchparameter)),
            map((treffer) => benutzerActions.bENUTZER_FOUND({ treffer }))
        );
    });

}