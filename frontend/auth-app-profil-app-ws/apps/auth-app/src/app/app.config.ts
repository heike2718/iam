import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { Configuration } from '@auth-app-profil-app-ws/config';
import { environment } from '../environments/environment';

registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(appRoutes),
    {
      provide: Configuration,
      useFactory: () =>
        new Configuration(
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
