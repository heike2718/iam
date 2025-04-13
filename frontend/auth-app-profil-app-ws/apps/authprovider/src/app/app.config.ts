import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection, ErrorHandler } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { environment } from '../environments/environment';
import { AuthproviderConfiguration } from '@authprovider/configuration';
import { provideStore } from '@ngrx/store';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { forgotPasswordDataProvider } from '@authprovider/forgot-password/api';
import { LoadingInterceptor } from '@ap-ws/messages/api';
import { APIHttpInterceptor } from './interceptors/api-http.interceptor';
import { ErrorHandlerService } from '@ap-ws/common-utils';
import { provideAnimations } from '@angular/platform-browser/animations';
import { changeTempPasswordDataProvider } from '@authprovider/change-temp-password/api';
import { loginSignupDataProvider } from '@authprovider/login-signup/api';


registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideAnimations(),
    provideRouter(appRoutes),
    provideStore({}),
    // provideHttpClient(withInterceptorsFromDi(), withXsrfConfiguration({
    //   cookieName: 'XSRF-TOKEN',
    //   headerName: 'X-XSRF-TOKEN',
    // })),
    provideHttpClient(withInterceptorsFromDi()),
    forgotPasswordDataProvider,
    changeTempPasswordDataProvider,
    loginSignupDataProvider,
    environment.providers,
    {
      provide: AuthproviderConfiguration,
      useFactory: () =>
        new AuthproviderConfiguration(
          environment.version,
          environment.envName,
          environment.baseUrl,
          environment.profilUrl,
          environment.datenschutzUrl,
          environment.assetsPath,
          environment.withCredentials,
          'authprovider',
          environment.production
        ),
    },    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de-DE',
    },
    { provide: LOCALE_ID, useValue: 'de-DE' },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: LoadingInterceptor },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: APIHttpInterceptor },
    { provide: ErrorHandler, useClass: ErrorHandlerService },
  ],
};

