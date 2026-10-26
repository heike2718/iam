import { DataSource } from '@angular/cdk/collections';
import { Injectable, inject } from '@angular/core';
import { BenutzerFacade } from './benutzer.facade';
import { Observable } from 'rxjs';
import { Benutzer } from '@bv-admin/shared/model';

@Injectable({
    providedIn: 'root',
})
export class BenutzerDataSource implements DataSource<Benutzer> {
    #benutzerFacade = inject(BenutzerFacade);

    connect(): Observable<readonly Benutzer[]> {
        return this.#benutzerFacade.page$;
    }

    disconnect(): void {
        // intesionylly left empty
    }
}
