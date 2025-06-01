import { Injectable, inject } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { Aktivierungsstatus, BenutzerSuchparameter, BenutzersucheFilterAndSortValues } from "@bv-admin/benutzer/model";
import { fromBenutzer, benutzerActions } from "@bv-admin/benutzer/data";
import { Benutzer, FlagsDto, PageDefinition, PaginationState, SortDefinition } from '@bv-admin/shared/model'


@Injectable({
    providedIn: 'root'
})
export class BenutzerFacade {

    #store = inject(Store);

    readonly page$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.page);
    readonly anzahlTreffer$: Observable<number> = this.#store.select(fromBenutzer.anzahlTreffer);
    readonly paginationState$: Observable<PaginationState> = this.#store.select(fromBenutzer.paginationState);
    readonly filterValues$: Observable<BenutzersucheFilterAndSortValues> = this.#store.select(fromBenutzer.filterValues);
    readonly sortDefinition$: Observable<SortDefinition> = this.#store.select(fromBenutzer.sortDefinition);

    readonly benutzerBasket$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.benutzerBasket);
    readonly lengthBenutzerBasket$: Observable<number> = this.#store.select(fromBenutzer.lengthBenutzerBasket);
    readonly uuidsGewaehlteBenutzer$: Observable<string[]> = this.#store.select(fromBenutzer.uuidsGewaehlteBenutzer);

    selectionsubsetChanged(actuallySelected: Benutzer[], actuallyDeselected: Benutzer[]): void {
        this.#store.dispatch(benutzerActions.sELECTIONSUBSET_CHANGED({ actuallySelected, actuallyDeselected }))
    }

    resetBenutzerBasket(): void {
        this.#store.dispatch(benutzerActions.rESET_BENUTZERBASKET());
    }

    removeBenutzerFromBasket(benutzer: Benutzer): void {
        this.#store.dispatch(benutzerActions.rEMOVE_SINGLE_BENUTZER_FROM_BASKET({ benutzer }));
    }

    benutzersucheChanged(filter: BenutzersucheFilterAndSortValues, pageDefinition: PageDefinition): void {
        this.#store.dispatch(benutzerActions.bENUTZER_PAGEDEFINITION_CHANGED({ pageDefinition }));
        this.#store.dispatch(benutzerActions.bENUTZER_FILTER_CHANGED({ filter }));
    }

    deleteSingleBenutzer(benutzer: Benutzer): void {
        this.#store.dispatch(benutzerActions.dELETE_SINGLE_BENUTZER({ benutzer }));
    }

    updateBenutzerFlags(benutzer: Benutzer, flagsDto: FlagsDto) {

        this.#store.dispatch(benutzerActions.uPDATE_BENUTZER_FLAGS({uuid: benutzer.uuid, flagsDto: flagsDto}));

    }

    triggerSearch(filter: BenutzersucheFilterAndSortValues, pageDefinition: PageDefinition): void {

        const sortDirection = pageDefinition.sortDirection !== null && pageDefinition.sortDirection === '' ? null : pageDefinition.sortDirection;

        const suchparameter: BenutzerSuchparameter = {
            aenderungsdatum: filter.aenderungsdatum,
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