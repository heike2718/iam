import { provideStoreDevtools } from "@ngrx/store-devtools";

export const environment = {
  version: '9.1.1',
  envName: 'docker',
  production: false,
  baseUrl: '',
  profilUrl: 'http://localhost:9600/benutzerprofil/',
  datenschutzUrl: 'https://mathe-jung-alt.de/minikaenguru/datenschutz.html',
  withCredentials: true,
  assetsPath: '/authprovider/assets/',
  providers: [
    provideStoreDevtools({
      maxAge: 25,
      autoPause: true,
      connectInZone: true
    }),
  ],
};