import { Injectable, inject } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable, filter } from "rxjs";
import { Benutzer, BenutzerSuchparameter, BenutzersucheFilterAndSortValues } from "@bv-admin-app/benutzer/model";
import { fromBenutzer, benutzerActions } from "@bv-admin-app/benutzer/data";
import { PageDefinition, PaginationState, SortDefinition } from '@bv-admin-app/shared/model'


@Injectable({
    providedIn: 'root'
})
export class BenutzerFacade {

    #store = inject(Store);

    page$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.page);
    anzahlTreffer$: Observable<number> = this.#store.select(fromBenutzer.anzahlTreffer);
    paginationState$: Observable<PaginationState> = this.#store.select(fromBenutzer.paginationState);
    benutzerBasket$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.benutzerBasket);
    filterValues$: Observable<BenutzersucheFilterAndSortValues> = this.#store.select(fromBenutzer.filterValues);
    sortDefinition$: Observable<SortDefinition> = this.#store.select(fromBenutzer.sortDefinition);

    selectionsubsetChanged(actuallySelected: Benutzer[], actuallyDeselected: Benutzer[]): void {
        this.#store.dispatch(benutzerActions.sELECTIONSUBSET_CHANGED({actuallySelected, actuallyDeselected}))
    }

    resetBenutzerBasket(): void {
        this.#store.dispatch(benutzerActions.bENUTZERBASKET_CHANGED({selection: []}));
    }
    

    #pagedefinitionChanged(pageDefinition: PageDefinition): void {
        this.#store.dispatch(benutzerActions.bENUTZER_PAGEDEFINITION_CHANGED({ pageDefinition }));
    }

    benutzersucheChanged(filter: BenutzersucheFilterAndSortValues, pageDefinition: PageDefinition): void {
        this.#store.dispatch(benutzerActions.bENUTZER_PAGEDEFINITION_CHANGED({ pageDefinition }));
        this.#store.dispatch(benutzerActions.bENUTZER_FILTER_CHANGED({ filter }));
    }

    #benutzersuchfilterChanged(filter: BenutzersucheFilterAndSortValues): void {
        this.#store.dispatch(benutzerActions.bENUTZER_FILTER_CHANGED({ filter }));
    }

    // benutzerBasketChanged(selection: Benutzer[]): void {
    //     this.#store.dispatch(benutzerActions.bENUTZERBASKET_CHANGED({selection}));
    // }

    triggerSearch(filter: BenutzersucheFilterAndSortValues, pageDefinition: PageDefinition): void {

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

        this.benutzersucheChanged(filter, pageDefinition);
        this.#store.dispatch(benutzerActions.fIND_BENUTZER({ suchparameter }));
    }

    resetFilterAndSort() {
        this.#store.dispatch(benutzerActions.rESET_FILTER());

    }
}