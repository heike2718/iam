import { Injectable, inject } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable, filter } from "rxjs";
import { Benutzer, BenutzerSuchparameter, BenutzerTableFilter, PageDefinition, PaginationState, initialBenutzerTableFilter, initialPageDefinition } from "@bv-admin-app/benutzer/model";
import { fromBenutzer, benutzerActions } from "@bv-admin-app/benutzer/data";


@Injectable({
    providedIn: 'root'
})
export class BenutzerFacade {

    #store = inject(Store);

    #benutzerFilter: BenutzerTableFilter = initialBenutzerTableFilter;
    #pageDefinition: PageDefinition = initialPageDefinition;

    constructor() {
        this.benutzerTableFilter$.subscribe((filter) => this.#benutzerFilter = filter);
        this.paginationState$.subscribe((paginationState) => this.#pageDefinition = paginationState.pageDefinition);
    }


    page$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.page);
    anzahlTreffer$: Observable<number> = this.#store.select(fromBenutzer.anzahlTreffer);
    benutzerTableFilter$: Observable<BenutzerTableFilter> = this.#store.select(fromBenutzer.benutzerTableFilter);
    paginationState$: Observable<PaginationState> = this.#store.select(fromBenutzer.paginationState);


    findBenutzer(): void {

        const suchparameter: BenutzerSuchparameter = {
            aktiviert: this.#benutzerFilter.aktiviert,
            dateModified: this.#benutzerFilter.dateModified,
            email: this.#benutzerFilter.email,
            nachname: this.#benutzerFilter.nachname,
            pageIndex: this.#pageDefinition.pageIndex,
            pageSize: this.#pageDefinition.pageSize,
            rolle: this.#benutzerFilter.rolle,
            sortByLabelname: this.#pageDefinition.sortByLabelname,
            sortDirection: this.#pageDefinition.sortDirection,
            uuid: this.#benutzerFilter.uuid,
            vorname: this.#benutzerFilter.vorname
        }

        this.#store.dispatch(benutzerActions.fIND_BENUTZER({suchparameter}));
    }

    benutzerFilterChanged(column: string, filterValue: string): void {

        let filterValues: BenutzerTableFilter = initialBenutzerTableFilter;

        switch(column) {
            case 'vorname': filterValues = {...initialBenutzerTableFilter, vorname: filterValue}; break;
            case 'nachname': filterValues = {...initialBenutzerTableFilter, nachname: filterValue}; break;
        }
        this.#store.dispatch(benutzerActions.bENUTZER_TABLE_FILTER_CHANGED({filterValues: filterValues}));
    }
}