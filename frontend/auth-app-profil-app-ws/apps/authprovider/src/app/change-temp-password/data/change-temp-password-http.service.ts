import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { ResponsePayload } from "@ap-ws/common-model";
import { ChangeTempPasswordPayload } from "@authprovider/model";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class ChangeTempPasswordHttpService {

    private url = '/authprovider/api/temppwd';
    
    #http = inject(HttpClient);

    changeTempPassword(changeTempPasswordPayload: ChangeTempPasswordPayload): Observable<ResponsePayload> {

		return this.#http.put<ResponsePayload>(this.url, changeTempPasswordPayload);
	}

}