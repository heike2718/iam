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
import { Configuration } from '@bv-admin-app/shared/config';

/**
 * Packt eine correlationId und die clientId in den Request
 */
@Injectable()
export class AuthAdminAPIHttpInterceptor implements HttpInterceptor {

    #config = inject(Configuration);

    intercept(
        req: HttpRequest<unknown>,
        next: HttpHandler
    ): Observable<HttpEvent<unknown>> {

        const url = this.#config.baseUrl + req.url;

        // console.log(url);
        // const headers = new HttpHeaders();

        const headers: HttpHeaders = req.headers
            .append('X-CLIENT-ID', this.#config.clientId)
            .append('Accept', 'application/json');

        // Es kam vor, dass das SessionCookie nicht im Client ankam.
        // 2 Bedingungen: 
        // 1. withCredentials muss true sein
        // 1. Der Name des SessionCookies muss mit JSESSIONID beginnen
        return next.handle(
            req.clone({
                headers: headers,
                url: url,
                withCredentials: true
            })
        ).pipe(
            catchError((error: HttpErrorResponse) => {
                // Rethrow the error to be handled by the global error handler
                return throwError(() => error);
            })
        );
    }
}
