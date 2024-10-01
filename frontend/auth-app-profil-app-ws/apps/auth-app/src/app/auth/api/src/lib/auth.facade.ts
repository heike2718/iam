import { inject, Injectable, Signal } from "@angular/core";
import { toSignal } from '@angular/core/rxjs-interop'; 
import { Store } from "@ngrx/store";
import { authActions } from "@auth-app/auth/data";
import { fromAuth } from "@auth-app/auth/data";
import { AuthSession, undefinedAuthSession } from "@auth-app/auth/model";

@Injectable({
    providedIn: 'root'
})
export class AuthFacade {

    #store = inject(Store);
    #authSession: Signal<AuthSession> = toSignal(this.#store.select(fromAuth.authSession), { initialValue: undefinedAuthSession} );

    get authSession() {
        return this.#authSession;
    }
    

    public createAnonymousSession() {
        this.#store.dispatch(authActions.cREATE_ANONYMOUS_SESSION());
    }

}