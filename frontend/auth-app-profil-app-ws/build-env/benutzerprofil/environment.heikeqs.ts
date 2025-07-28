import { provideStoreDevtools } from "@ngrx/store-devtools";

export const environment = {
  version: '9.1.1',
  envName: 'dev',
  production: false,
  baseUrl: '',
  loginRedirectUrl: 'http://heikeqs/benutzerprofil/',
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