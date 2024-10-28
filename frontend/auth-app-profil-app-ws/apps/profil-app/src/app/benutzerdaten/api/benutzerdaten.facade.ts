import { inject, Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import { benutzerdatenActions, fromBenutzerdaten } from "../data";
import { Observable } from "rxjs";
import { Benutzerdaten } from "../model";

@Injectable({
    providedIn: 'root'
})
export class BenutzerdatenFacade {

    #store = inject(Store);

    benutzerdaten$: Observable<Benutzerdaten> = this.#store.select(fromBenutzerdaten.benutzerdaten);

    public benutzerdatenLaden(): void {
        
        this.#store.dispatch(benutzerdatenActions.lOAD_BENUTZERDATEN());
    }

    public handleLogout(): void {

        this.#store.dispatch(benutzerdatenActions.rESET_BENUTZERDATEN());
    }


}