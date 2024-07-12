import { inject, Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { Infomail } from '@bv-admin-app/infomails/model';
import { fromInfomails, infomailsActions } from '@bv-admin-app/infomails/data';
import { filterDefined } from "@bv-admin-app/shared/util";


@Injectable({
    providedIn: 'root'
})
export class InfomailsFacade {

    #store = inject(Store);

    readonly infomailsLoaded$: Observable<boolean> = this.#store.select(fromInfomails.infomailsLoaded);
    readonly infomails$: Observable<Infomail[]> = this.#store.select(fromInfomails.infomails);
    readonly selectedInfomail$: Observable<Infomail> = this.#store.select(fromInfomails.selectedInfomail).pipe(filterDefined);

    loadInfomails(): void {
        this.#store.dispatch(infomailsActions.lOAD_INFOMAILS());
    }

    selectInfomail(infomail: Infomail): void {
        this.#store.dispatch(infomailsActions.iNFOMAIL_SELECTED({infomail}));
    }

    clearInfomails(): void {
        this.#store.dispatch(infomailsActions.cLEAR_INFOMAILS());
    }

}