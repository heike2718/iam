import { Injectable, inject } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { Aktivierungsstatus, BenutzerSuchparameter, BenutzersucheFilterAndSortValues } from "@bv-admin-app/benutzer/model";
import { fromBenutzer, benutzerActions } from "@bv-admin-app/benutzer/data";
import { Benutzer, PageDefinition, PaginationState, SortDefinition } from '@bv-admin-app/shared/model'


@Injectable({
    providedIn: 'root'
})
export class BenutzerFacade {

    #store = inject(Store);

    page$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.page);
    anzahlTreffer$: Observable<number> = this.#store.select(fromBenutzer.anzahlTreffer);
    paginationState$: Observable<PaginationState> = this.#store.select(fromBenutzer.paginationState);
    benutzerBasket$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.benutzerBasket);
    lengthBenutzerBasket$: Observable<number> = this.#store.select(fromBenutzer.lengthBenutzerBasket);
    filterValues$: Observable<BenutzersucheFilterAndSortValues> = this.#store.select(fromBenutzer.filterValues);
    sortDefinition$: Observable<SortDefinition> = this.#store.select(fromBenutzer.sortDefinition);

    selectionsubsetChanged(actuallySelected: Benutzer[], actuallyDeselected: Benutzer[]): void {
        this.#store.dispatch(benutzerActions.sELECTIONSUBSET_CHANGED({actuallySelected, actuallyDeselected}))
    }

    resetBenutzerBasket(): void {
        this.#store.dispatch(benutzerActions.rESET_BENUTZERBASKET());
    }

    removeBenutzerFromBasket(benutzer: Benutzer): void {
        this.#store.dispatch(benutzerActions.rEMOVE_SINGLE_BENUTZER_FROM_BASKET({benutzer}));
    }
    
    benutzersucheChanged(filter: BenutzersucheFilterAndSortValues, pageDefinition: PageDefinition): void {
        this.#store.dispatch(benutzerActions.bENUTZER_PAGEDEFINITION_CHANGED({ pageDefinition }));
        this.#store.dispatch(benutzerActions.bENUTZER_FILTER_CHANGED({ filter }));
    }

    deleteSingleBenutzer(benutzer: Benutzer): void {
        this.#store.dispatch(benutzerActions.dELETE_SINGLE_BENUTZER({benutzer}));
    }

    updateBenutzerAktivierungsstatue(benutzer: Benutzer, aktiviert: boolean): void {

        const neuerAktivierungsstatus: Aktivierungsstatus = {aktiviert};
        this.#store.dispatch(benutzerActions.uPDATE_BENUTZER_ACTIVATION_STATE({uuid: benutzer.uuid, aktivierungsstatus: neuerAktivierungsstatus}));

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