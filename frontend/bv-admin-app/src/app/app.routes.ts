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
          import('@bv-admin-app/users/feature').then((m) => m.usersRoutes),
    },
    {
        path: '**',
        component: HomeComponent
    }

];
