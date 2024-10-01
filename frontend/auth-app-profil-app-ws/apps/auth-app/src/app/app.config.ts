import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { environment } from '../environments/environment';
import { AuthAppConfiguration } from './config/auth-app.configuration';
import { provideStore } from '@ngrx/store';
import { authDataProvider } from '@auth-app/auth/api';
import { HttpClientModule } from '@angular/common/http';

registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(appRoutes),
    provideStore({}),
    authDataProvider,
    importProvidersFrom(
      HttpClientModule
    ),
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
  ],
};
