import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AuthHttpService } from "../auth-http.service";
import { authActions } from "./auth.actions";
import { map, of, switchMap, tap } from "rxjs";
import { Message } from "@ap-ws/common-model";
import { Session } from "apps/profil-app/src/app/auth/model";
import { Router } from '@angular/router';
import { BenutzerdatenFacade } from "@profil-app/benutzerdaten/api";


@Injectable({
    providedIn: 'root'
})
export class AuthEffects {

    #actions = inject(Actions);
    #authHttpService = inject(AuthHttpService);
    #router = inject(Router);
    #benutzerdatenFacade = inject(BenutzerdatenFacade);

    requestLoginUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.rEQUEST_LOGIN_URL),
            switchMap(() => this.#authHttpService.getLoginUrl()),
            map((message: Message) => authActions.rEDIRECT_TO_AUTH({ authUrl: message.message }))
        );
    });

    redirectToAuth$ = createEffect(() =>

        this.#actions.pipe(
            ofType(authActions.rEDIRECT_TO_AUTH),
            switchMap((action) => of(action.authUrl)),
            tap((authUrl) => window.location.href = authUrl)
        ), { dispatch: false });


    createSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.cREATE_SESSION),
            switchMap(({ authResult }) =>
                this.#authHttpService.createSession(authResult)
            ),
            map((session: Session) => authActions.sESSION_CREATED({ session }))
        );
    });

    logOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.lOG_OUT),
            switchMap(() =>
                this.#authHttpService.logOut()
            ),
            map(() => authActions.lOGGED_OUT())
        );
    });

    loggedOut$ = createEffect(() =>
        this.#actions.pipe(
            ofType(authActions.lOGGED_OUT),
            tap(() => {
                this.#benutzerdatenFacade.handleLogout();
                this.#router.navigateByUrl('/');
            })
        ), { dispatch: false });

    sessionCreated$ = createEffect(() =>
        this.#actions.pipe(
            ofType(authActions.sESSION_CREATED),
            tap(() => this.#benutzerdatenFacade.benutzerdatenLaden())
        ), { dispatch: false });

}
