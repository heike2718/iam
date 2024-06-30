import { Injectable, inject } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable, filter } from "rxjs";
import { Benutzer, BenutzerSuchparameter, BenutzersucheFilterValues } from "@bv-admin-app/benutzer/model";
import { fromBenutzer, benutzerActions } from "@bv-admin-app/benutzer/data";
import { PageDefinition, PaginationState } from '@bv-admin-app/shared/model'


@Injectable({
    providedIn: 'root'
})
export class BenutzerFacade {

    #store = inject(Store);

    page$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.page);
    anzahlTreffer$: Observable<number> = this.#store.select(fromBenutzer.anzahlTreffer);
    paginationState$: Observable<PaginationState> = this.#store.select(fromBenutzer.paginationState);
    benutzerBasket$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.benutzerBasket);
    filterValues$: Observable<BenutzersucheFilterValues> = this.#store.select(fromBenutzer.filterValues);

    selectionsubsetChanged(actuallySelected: Benutzer[], actuallyDeselected: Benutzer[]): void {
        this.#store.dispatch(benutzerActions.sELECTIONSUBSET_CHANGED({actuallySelected, actuallyDeselected}))
    }

    // benutzerBasketChanged(selection: Benutzer[]): void {
    //     this.#store.dispatch(benutzerActions.bENUTZERBASKET_CHANGED({selection}));
    // }

    triggerSearch(filter: BenutzersucheFilterValues, pageDefinition: PageDefinition): void {

        const sortDirection = pageDefinition.sortDirection !== null && pageDefinition.sortDirection === '' ? null : pageDefinition.sortDirection;

        const suchparameter: BenutzerSuchparameter = {
            aktiviert: filter.aktiviert,
            dateModified: filter.dateModified,
            email: filter.email,
            nachname: filter.nachname,
            pageIndex: pageDefinition.pageIndex,
            pageSize: pageDefinition.pageSize,
            rolle: filter.rolle,
            sortByLabelname: filter.sortByLabelname,
            sortDirection: sortDirection,
            uuid: filter.uuid,
            vorname: filter.vorname
        };

        this.#store.dispatch(benutzerActions.bENUTZER_FILTER_CHANGED({ filter }));
        this.#store.dispatch(benutzerActions.bENUTZER_SELECT_PAGE({ pageDefinition }));
        this.#store.dispatch(benutzerActions.fIND_BENUTZER({ suchparameter }));
    }

    resetFilterAndSort() {
        this.#store.dispatch(benutzerActions.rESET_FILTER());

    }
}