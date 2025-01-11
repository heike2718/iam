import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { TempPasswordCredentials, TempPasswordResponseDto } from "@authprovider/model";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class ForgotPasswordHttpService {

    private url = '/authprovider/api/temppwd';
    
    #http = inject(HttpClient);

    orderTempPassword(tempPasswordCredentials: TempPasswordCredentials): Observable<TempPasswordResponseDto> {

		return this.#http.post<TempPasswordResponseDto>(this.url, tempPasswordCredentials);
	}

}