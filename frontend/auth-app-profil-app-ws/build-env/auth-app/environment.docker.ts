import { provideStoreDevtools } from "@ngrx/store-devtools";

export const environment = {
  version: '9.0.0',
  envName: 'docker',
  production: false,
  baseUrl: '',
  profilUrl: 'http://benutzerverwaltung:9600/benutzerverwaltung/',
  datenschutzUrl: 'https://mathe-jung-alt.de/minikaenguru/datenschutz.html',
  withCredentials: true,
  assetsPath: '/auth-app/public/',
  providers: [
    provideStoreDevtools({
      maxAge: 25,
      autoPause: true,
      connectInZone: true
    }),
  ],
};