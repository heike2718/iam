import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { LoadingIndicatorService } from "./loading-indicator.service";
import { catchError, Observable, tap, throwError } from "rxjs";
import { SILENT_LOAD_CONTEXT } from "./silent-load.context";


@Injectable()
export class LoadingInterceptor implements HttpInterceptor {
    
    constructor(private loadingIndicatorService: LoadingIndicatorService){}

    intercept(
        req: HttpRequest<unknown>,
        next: HttpHandler
    ): Observable<HttpEvent<unknown>> {
        if (req.context.get(SILENT_LOAD_CONTEXT)) {
            return next.handle(req);
        }

        this.loadingIndicatorService.start();
        return next.handle(req).pipe(
            tap((event) => {
                if (event instanceof HttpResponse) {
                    this.loadingIndicatorService.stop();
                }
            }),
            catchError((err) => {
                this.loadingIndicatorService.stop();
                return throwError(() => err);
            })
        );
    }
}