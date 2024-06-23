import { Injectable, inject } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable, filter } from "rxjs";
import { Benutzer, BenutzerSuchparameter, BenutzersucheGUIModel, initialBenutzersucheGUIModel } from "@bv-admin-app/benutzer/model";
import { fromBenutzer, benutzerActions } from "@bv-admin-app/benutzer/data";


@Injectable({
    providedIn: 'root'
})
export class BenutzerFacade {

    #store = inject(Store);

    page$: Observable<Benutzer[]> = this.#store.select(fromBenutzer.page);
    anzahlTreffer$: Observable<number> = this.#store.select(fromBenutzer.anzahlTreffer);
    guiModel$: Observable<BenutzersucheGUIModel> = this.#store.select(fromBenutzer.guiModel);

    startSuche(guiModel: BenutzersucheGUIModel): void {

        this.guiModelChanged(guiModel);

        const suchparameter: BenutzerSuchparameter = {
            aktiviert: guiModel.aktiviert,
            dateModified: guiModel.dateModified,
            email: guiModel.email,
            nachname: guiModel.nachname,
            pageIndex: guiModel.pageIndex,
            pageSize: guiModel.pageSize,
            rolle: guiModel.rolle,
            sortByLabelname: guiModel.sortByLabelname,
            sortDirection: guiModel.sortDirection,
            uuid: guiModel.uuid,
            vorname: guiModel.vorname
        }

        this.#store.dispatch(benutzerActions.fIND_BENUTZER({ suchparameter }));
    }

    guiModelChanged(guiModel: BenutzersucheGUIModel): void {
        this.#store.dispatch(benutzerActions.gUI_MODEL_CHANGED({ guiModel }));
    }

    resetFilterAndSort() {
        this.#store.dispatch(benutzerActions.sUCHE_ZURUECKSETZEN());

    }
}