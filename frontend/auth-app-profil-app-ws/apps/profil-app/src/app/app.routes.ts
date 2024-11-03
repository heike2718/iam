import { Route } from '@angular/router';
import { BenutzerdatenComponent } from './benutzerdaten/feature/benutzerdaten-component/benutzerdaten.component';
import { ChangePasswortComponent } from './passwort/feature/change-passwort-component/change-passwort.component';
import { KontoLoeschenComponent } from './benutzerdaten/feature/konto-loeschen-component/konto-loeschen.component';
import { HomeComponent } from './home-component/home.component';
import { inject } from '@angular/core';
import { AuthFacade } from './auth/api/auth.facade';

export const appRoutes: Route[] = [

    {
        path: 'home',
        component: HomeComponent
    },
    {
        path: 'benutzerdaten',
        canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
        component: BenutzerdatenComponent
    },
    {
        path: 'passwort',
        canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
        component: ChangePasswortComponent
    },
    {
        path: 'loeschung-benutzerkonto',
        canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
        component: KontoLoeschenComponent
    },
    {
        path: '',
        component: HomeComponent
    },
    {
        path: '**',
        component: HomeComponent
    }
];
