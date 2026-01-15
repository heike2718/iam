import { inject, Injectable } from "@angular/core";
import { ChangeTempPasswordPayload } from '../../model/auth.model';
import { Store } from "@ngrx/store";
import { changeTempPasswordActions } from '../data/+state/change-temp-password.actions';
import { fromChangeTempPassword } from '../data/+state/change-temp-password.selectors';
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ChangeTempPasswordFacade {

    #store = inject(Store);

    tempPasswordSuccess$: Observable<boolean> = this.#store.select(fromChangeTempPassword.tempPasswordSuccess);
    tempPasswordSuccessMessage$: Observable<string | undefined> = this.#store.select(fromChangeTempPassword.tempPasswordSuccessMessage);

    public changeTempPassword(changeTempPasswordPayload: ChangeTempPasswordPayload): void {

        this.#store.dispatch(changeTempPasswordActions.cHANGE_TEMP_PASSWORD({ changeTempPasswordPayload}));
    }

    public onDestroyComponent(): void {

        this.#store.dispatch(changeTempPasswordActions.rESET_CHANGE_TEMP_PASSWORD_STATE());
    }
}
