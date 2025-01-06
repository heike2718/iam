import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection, importProvidersFrom, ErrorHandler } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { environment } from '../environments/environment';
import { AuthAppConfiguration } from './config/auth-app.configuration';
import { provideStore } from '@ngrx/store';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { forgotPasswordDataProvider } from '@auth-app/forgot-password/api';
import { LoadingInterceptor } from '@ap-ws/messages/api';
import { APIHttpInterceptor } from './interceptors/api-http.interceptor';
import { ErrorHandlerService } from '@ap-ws/common-utils';
import { provideAnimations } from '@angular/platform-browser/animations';
import { changeTempPasswordDataProvider } from '@auth-app/change-temp-password/api';
import { loginSignupDataProvider } from '@auth-app/login-signup/api';


registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideAnimations(),
    provideRouter(appRoutes),
    provideStore({}),
    provideHttpClient(withInterceptorsFromDi()),
    forgotPasswordDataProvider,
    changeTempPasswordDataProvider,
    loginSignupDataProvider,
    environment.providers,
    {
      provide: AuthAppConfiguration,
      useFactory: () =>
        new AuthAppConfiguration(
          environment.version,
          environment.envName,
          environment.baseUrl,
          environment.profilUrl,
          environment.datenschutzUrl,
          environment.assetsPath,
          environment.withCredentials,
          'auth-app',
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

