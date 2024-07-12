import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { InfomailsHttpService  } from '../infomails-http.service';
import { MessageService } from "@bv-admin-app/shared/messages/api";
import { infomailsActions } from "./infomails.actions";
import { map, switchMap } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class InfomailsEffects {

    #actions = inject(Actions);
    #httpService = inject(InfomailsHttpService);
    #messageService = inject(MessageService);

    loadInfomailTexte$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(infomailsActions.lOAD_INFOMAILS),
            switchMap((action) => this.#httpService.loadInfomailTexte()),
            map((infomails) => infomailsActions.iNFOMAILS_LOADED({infomails}))
        );
    });

}