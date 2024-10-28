import { Route } from '@angular/router';
import { BenutzerdatenComponent } from './benutzerdaten/feature/benutzerdaten-component/benutzerdaten.component';
import { PasswortComponent } from './passwort/passwort-component/passwort.component';
import { KontoLoeschenComponent } from './konto-loeschen/konto-loeschen-component/konto-loeschen.component';

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
        component: BenutzerdatenComponent
    }
];
