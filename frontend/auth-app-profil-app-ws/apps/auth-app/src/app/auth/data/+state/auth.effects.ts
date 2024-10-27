import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { AuthHttpService } from "../auth-http.service";
import { authActions } from "./auth.actions";
import { map, switchMap } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthEffects {

    #actions = inject(Actions);
    #httpService = inject(AuthHttpService);

    createAnonymousSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.cREATE_ANONYMOUS_SESSION),
            switchMap(() => this.#httpService.createAnonymousSession()),
            map((authSession) => authActions.aNONYMOUS_SESSION_CREATED({ authSession: authSession }))
        );
    });

}