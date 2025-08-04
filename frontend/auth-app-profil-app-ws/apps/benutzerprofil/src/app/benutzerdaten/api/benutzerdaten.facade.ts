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
    isExistierenderBenutzer$: Observable<boolean> = this.#store.select(fromBenutzerdaten.isExistierenderBenutzer);
    isBenutzerLoaded$: Observable<boolean> = this.#store.select(fromBenutzerdaten.isBenutzerLoaded);

    public benutzerdatenLaden(): void {
        this.#store.dispatch(benutzerdatenActions.lOAD_BENUTZERDATEN());
    }

    public benutzerdatenAendern(benutzerdaten: Benutzerdaten): void {

        this.#store.dispatch(benutzerdatenActions.bENUTZERDATEN_AENDERN({benutzerdaten}));
    }

    public benutzerkontoLoeschen(): void {
        this.#store.dispatch(benutzerdatenActions.kONTO_LOESCHEN());
    }

    public handleLogout(): void {

        this.#store.dispatch(benutzerdatenActions.rESET_BENUTZERDATEN());
    }




}