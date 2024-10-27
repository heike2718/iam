import { Route } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PasswortComponent } from './passwort-component/passwort.component';
import { KontoLoeschenComponent } from './konto-loeschen-component/konto-loeschen.component';

export const appRoutes: Route[] = [

    {
        path: 'passwort',
        component: PasswortComponent
    },
    {
        path: 'loeschung-benutzerkonto',
        component: KontoLoeschenComponent
    },
    {
        path: '**',
        component: HomeComponent
    }
];
