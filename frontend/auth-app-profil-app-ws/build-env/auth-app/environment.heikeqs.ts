import { provideStoreDevtools } from '@ngrx/store-devtools';

export const environment = {
  version: '9.0.0',
  envName: 'heikeqs',
  production: false,
  baseUrl: '',
  profilUrl: 'http://heikeqs/profil-app/',
  datenschutzUrl: 'https://mathe-jung-alt.de/minikaenguru/datenschutz.html',
  withCredentials: true,
  assetsPath: '/auth-app/public/',
  providers: [
    provideStoreDevtools({ maxAge: 25 , connectInZone: true})
  ],
};