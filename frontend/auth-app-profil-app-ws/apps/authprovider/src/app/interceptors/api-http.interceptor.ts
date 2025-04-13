import {
    HttpErrorResponse,
    HttpEvent,
    HttpHandler,
    HttpHeaders,
    HttpInterceptor,
    HttpRequest,
} from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthproviderConfiguration } from '@authprovider/configuration';


/**
 * Packt die clientId in den Request
 */
@Injectable()
export class APIHttpInterceptor implements HttpInterceptor {

    #config = inject(AuthproviderConfiguration);

    intercept(
        req: HttpRequest<unknown>,
        next: HttpHandler
    ): Observable<HttpEvent<unknown>> {

        const url = this.#config.baseUrl + req.url;

        console.log('>>>>> call ' + url);

        const headers: HttpHeaders = req.headers
            .append('X-CLIENT-ID', this.#config.clientId)
            .append('Accept', 'application/json');

        // withCredentials muss true sein, damit der X-XSRF-Token-Header gesetzt wird.
        return next.handle(
            req.clone({
                headers: headers,
                url: url,
                withCredentials: this.#config.withCredentials
            })
        ).pipe(
            catchError((error: HttpErrorResponse) => {
                // Rethrow the error to be handled by the global error handler
                return throwError(() => error);
            })
        );
    }
}
