import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { Injectable, inject } from "@angular/core";
import { BenutzerFacade } from "./benutzer.facade";
import { Observable } from "rxjs";
import { swallowEmptyArgument } from "@bv-admin-app/shared/util";
import { Benutzer } from "@bv-admin-app/shared/model";


@Injectable({
    providedIn: 'root'
})
export class BenutzerDataSource implements DataSource<Benutzer> {

    #benutzerFacade = inject(BenutzerFacade);

    connect(_collectionViewer: CollectionViewer): Observable<readonly Benutzer[]> {
        swallowEmptyArgument(_collectionViewer, false);
        return this.#benutzerFacade.page$;
    }

    disconnect(_collectionViewer: CollectionViewer): void {
        // hängt am Store muss also nicht finalized werden?
        swallowEmptyArgument(_collectionViewer, false);
    }


}