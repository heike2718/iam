import { Route } from '@angular/router';
import { BenutzerdatenComponent } from './benutzerdaten/feature/benutzerdaten-component/benutzerdaten.component';
import { ChangePasswortComponent } from './passwort/feature/change-passwort-component/change-passwort.component';
import { KontoLoeschenComponent } from './konto-loeschen/konto-loeschen-component/konto-loeschen.component';

export const appRoutes: Route[] = [

    {
        path: 'passwort',
        component: ChangePasswortComponent
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
