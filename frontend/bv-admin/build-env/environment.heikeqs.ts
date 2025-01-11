import { provideStoreDevtools } from '@ngrx/store-devtools';

// TODO: das muss mit /bv-admin getestet werden!!
export const environment = {
  production: false,
  baseUrl: '',
  withCredentials: true,
  assetsPath: '/bv-admin/assets/',
  providers: [
    provideStoreDevtools({ maxAge: 25 , connectInZone: true})
  ],
};