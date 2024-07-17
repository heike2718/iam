import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

export const appRoutes: Routes = [

    {
        path: '',
        component: HomeComponent
    },
    {
        path: 'home',
        component: HomeComponent
    },
    {
        path: 'users',
        loadChildren: () =>
          import('@bv-admin-app/benutzer/feature').then((m) => m.benutzerRoutes),
    },
    {
        path: 'infomails',
        loadChildren: () =>
          import('@bv-admin-app/infomails/feature').then((m) => m.infomailsRoutes),
    },
    {
        path: 'versandauftraege',
        loadChildren: () =>
          import('@bv-admin-app/versandauftraege/feature').then((m) => m.versandauftraegeRoutes),
    },
    {
        path: '**',
        component: HomeComponent
    }

];
