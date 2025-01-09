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
          import('@bv-admin/benutzer/feature').then((m) => m.benutzerRoutes),
    },
    {
        path: 'infomails',
        loadChildren: () =>
          import('@bv-admin/infomails/feature').then((m) => m.infomailsRoutes),
    },
    {
        path: 'versandauftraege',
        loadChildren: () =>
          import('@bv-admin/versandauftraege/feature').then((m) => m.versandauftraegeRoutes),
    },
    {
        path: '**',
        component: HomeComponent
    }

];
