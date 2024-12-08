import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { ResponsePayload } from "@ap-ws/common-model";
import { ChangeTempPasswordPayload } from "@auth-app/model";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class ChangeTempPasswordHttpService {

    private url = '/temppwd/v2';
    
    #http = inject(HttpClient);

    changeTempPassword(changeTempPasswordPayload: ChangeTempPasswordPayload): Observable<ResponsePayload> {

		return this.#http.put<ResponsePayload>(this.url, changeTempPasswordPayload);
	}

}