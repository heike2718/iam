import { inject, Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import { authActions, fromAuth } from "@profil-app/auth/data";
import { Observable, of, switchMap } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthFacade {

    #store = inject(Store);

    readonly userIsLoggedIn$: Observable<boolean> = this.#store.select(fromAuth.isLoggedIn);

    readonly userIsLoggedOut$: Observable<boolean> = this.userIsLoggedIn$.pipe(
        switchMap((li) => of(!li))
    );



    public login(): void {

        this.#store.dispatch(authActions.rEQUEST_LOGIN_URL());
    }

    public logout(): void {

        this.#store.dispatch(authActions.lOG_OUT());
    }



}