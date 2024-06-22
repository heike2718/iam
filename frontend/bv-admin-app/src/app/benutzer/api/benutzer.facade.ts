import { Injectable, inject } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { Benutzer, BenutzerSuchparameter, BenutzerTableFilter, PageDefinition } from "@bv-admin-app/benutzer/model";
import { fromBenutzer, benutzerActions } from "@bv-admin-app/benutzer/data";



@Injectable({
    providedIn: 'root'
})
export class BenutzerFacade {

    #store = inject(Store);

    page$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.page);
    anzahlTreffer$: Observable<number> = this.#store.select(fromBenutzer.anzahlTreffer);
    benutzerTableFilter$: Observable<BenutzerTableFilter> = this.#store.select(fromBenutzer.benutzerTableFilter);


    findBenutzer(filterValues: BenutzerTableFilter, pageDefinition: PageDefinition) {

        const suchparameter: BenutzerSuchparameter = {
            aktiviert: filterValues.aktiviert,
            dateModified: filterValues.dateModified,
            email: filterValues.email,
            nachname: filterValues.nachname,
            pageIndex: pageDefinition.pageIndex,
            pageSize: pageDefinition.pageSize,
            rolle: filterValues.rolle,
            sortByLabelname: pageDefinition.sortByLabelname,
            sortDirection: pageDefinition.sortDirection,
            uuid: filterValues.uuid,
            vorname: filterValues.vorname
        }

        this.#store.dispatch(benutzerActions.fIND_BENUTZER({suchparameter}));
    }
}