import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { environment } from '../environments/environment';
import { ProfilAppConfiguration } from './configuration/profil-app.configuration';
import { provideAnimations } from '@angular/platform-browser/animations';

registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideAnimations(),
    provideRouter(appRoutes),
    {
      provide: ProfilAppConfiguration,
      useFactory: () =>
        new ProfilAppConfiguration(
          environment.version,
          environment.envName,
          environment.baseUrl,
          environment.loginRedirectUrl,
          environment.datenschutzUrl,
          environment.assetsPath,
          environment.withCredentials,
          'profil-app',
          environment.production
        ),
    },    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de-DE',
    },
    { provide: LOCALE_ID, useValue: 'de-DE' },  ],
};
