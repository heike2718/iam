import { ApplicationConfig, ErrorHandler, LOCALE_ID, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { Configuration } from '@bv-admin-app/shared/config';
import { environment } from 'src/environments/environment';
import { provideAnimations } from '@angular/platform-browser/animations';
import { LoadingInterceptor } from '@bv-admin-app/shared/messages/api';
import { AuthAdminAPIHttpInterceptor, ErrorInterceptor } from '@bv-admin-app/shared/http';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { ErrorHandlerService } from './shell/services/error-handler.service';
import { localStorageReducer, loggedOutMetaReducer, LocalStorageEffects } from '@bv-admin-app/shared/local-storage-data';
import { provideRouterStore } from '@ngrx/router-store';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { authDataProvider } from '@bv-admin-app/shared/auth/api';
import { benutzerDataProvider } from '@bv-admin-app/benutzer/api';


const localStorageMetaReducer = localStorageReducer(
  'bvAuth'
); // <-- synchronisiert diese Slices des Store mit localStorage wegen F5.

const clearStoreMetaReducer = loggedOutMetaReducer;

const allMetaReducers = environment.production
  ? [localStorageMetaReducer]
  : [localStorageMetaReducer, clearStoreMetaReducer];

registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    authDataProvider,
    provideAnimations(),
    provideRouter(appRoutes),
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de-DE',
    },
    { provide: LOCALE_ID, useValue: 'de-DE' },
    {
      provide: Configuration,
      useFactory: () =>
        new Configuration(
          environment.baseUrl,
          environment.assetsPath,
          'bv-admin-app',
          environment.production
        ),
    },
    provideRouterStore(),

    /** das muss so gemacht werden, weil ohne den Parameter {} nichts da ist, wohinein man den state hängen könnte */
    provideStore(
      {
        
      },
      {
        metaReducers: allMetaReducers,
      }
    ),
    provideEffects(LocalStorageEffects),
    environment.providers,
    benutzerDataProvider,
    importProvidersFrom(
      HttpClientModule,
      HttpClientXsrfModule.withOptions({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      })
    ),
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: LoadingInterceptor },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: AuthAdminAPIHttpInterceptor },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: ErrorInterceptor },
    { provide: ErrorHandler, useClass: ErrorHandlerService },
  ],
};
