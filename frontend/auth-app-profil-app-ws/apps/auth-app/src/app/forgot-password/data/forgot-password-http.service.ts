import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { TempPasswordCredentials, TempPasswordResponseDto } from "@auth-app/model";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class ForgotPasswordHttpService {

    private url = '/temppwd';
    
    #http = inject(HttpClient);

    orderTempPassword(tempPasswordCredentials: TempPasswordCredentials): Observable<TempPasswordResponseDto> {

		return this.#http.post<TempPasswordResponseDto>(this.url, tempPasswordCredentials);
	}

}