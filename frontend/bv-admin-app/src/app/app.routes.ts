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
          import('src/app/benutzer/feature').then((m) => m.benutzerRoutes),
    },
    {
        path: '**',
        component: HomeComponent
    }

];
