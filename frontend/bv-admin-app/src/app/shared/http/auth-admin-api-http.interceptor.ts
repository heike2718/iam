import {
    HttpEvent,
    HttpHandler,
    HttpHeaders,
    HttpInterceptor,
    HttpRequest,
} from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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

        const headers: HttpHeaders = req.headers.append('X-CLIENT-ID', this.#config.clientId);

        return next.handle(
            req.clone({
                headers: headers,
                url: url
            })
        );
    }
}
