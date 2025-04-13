import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection, importProvidersFrom, ErrorHandler } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { environment } from '../environments/environment';
import { BenutzerprofilConfiguration } from '@benutzerprofil/configuration';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouterStore } from '@ngrx/router-store';
import { provideStore } from '@ngrx/store';
import { authDataProvider } from '@benutzerprofil/auth/api';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi, withXsrfConfiguration } from '@angular/common/http';
import { LoadingInterceptor } from '@ap-ws/messages/api';
import { APIHttpInterceptor } from './interceptors/api-http.interceptor';
import { ErrorHandlerService } from '@ap-ws/common-utils';
import { localStorageReducer, loggedOutMetaReducer, LocalStorageEffects } from './local-storage-data';
import { provideEffects } from '@ngrx/effects';
import { benutzerdatenDataProvider } from '@benutzerprofil/benutzerdaten/api';
import { passwortDataProvider } from '@benutzerprofil/passwort/api';

const localStorageMetaReducer = localStorageReducer(
  'profilAuth'
); // <-- synchronisiert diese Slices des Store mit localStorage wegen F5.
// auth = auth.reducer.ts/profilAuth

const clearStoreMetaReducer = loggedOutMetaReducer;

const allMetaReducers = environment.production
  ? [localStorageMetaReducer]
  : [localStorageMetaReducer, clearStoreMetaReducer];

registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideAnimations(),
    provideRouter(appRoutes),
    provideRouterStore(),
    provideStore(
      {

      },
      {
        metaReducers: allMetaReducers,
      }),
    provideEffects(LocalStorageEffects),
    provideHttpClient(withInterceptorsFromDi(), withXsrfConfiguration({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN',
    })),
    // provideHttpClient(withInterceptorsFromDi()),
    authDataProvider,
    benutzerdatenDataProvider,
    passwortDataProvider,
    environment.providers,
    {
      provide: BenutzerprofilConfiguration,
      useFactory: () =>
        new BenutzerprofilConfiguration(
          environment.version,
          environment.envName,
          environment.baseUrl,
          environment.loginRedirectUrl,
          environment.datenschutzUrl,
          environment.assetsPath,
          environment.withCredentials,
          'benutzerprofil',
          environment.production
        ),
    }, {
      provide: MAT_DATE_LOCALE,
      useValue: 'de-DE',
    },
    { provide: LOCALE_ID, useValue: 'de-DE' },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: LoadingInterceptor },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: APIHttpInterceptor },
    { provide: ErrorHandler, useClass: ErrorHandlerService },
  ],
};
