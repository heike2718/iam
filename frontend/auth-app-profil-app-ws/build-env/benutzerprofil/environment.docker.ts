import { provideStoreDevtools } from '@ngrx/store-devtools';

export const environment = {
  version: '9.1.0',
  envName: 'docker',
  production: false,
  baseUrl: '',
  loginRedirectUrl: 'http://localhost:9600/benutzerprofil/',
  datenschutzUrl: 'https://mathe-jung-alt.de/minikaenguru/datenschutz.html',
  withCredentials: true,
  assetsPath: '/benutzerprofil/assets/',
  providers: [
    provideStoreDevtools({
      maxAge: 25,
      autoPause: true,
      connectInZone: true
    }),
  ],
};