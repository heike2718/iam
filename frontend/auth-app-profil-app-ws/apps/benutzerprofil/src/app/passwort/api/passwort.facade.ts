import { inject, Injectable } from "@angular/core";
import { PasswortPayload } from "@ap-ws/common-model";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { fromPasswort } from '../data/+state/passwort.selectors';
import { passwortActions } from '../data/+state/passwort.actions';

@Injectable({
    providedIn: 'root'
})
export class PasswortFacade {

    #store = inject(Store);

    passwort$: Observable<PasswortPayload> = this.#store.select(fromPasswort.passwort);

    public passwortAendern(passwortPayload: PasswortPayload): void {
        this.#store.dispatch(passwortActions.pASSWORT_AENDERN({passwortPayload}));
    }

    public passwortWegraeumen(): void {
        this.#store.dispatch(passwortActions.rESET_PASSWORT());
    }
    
}