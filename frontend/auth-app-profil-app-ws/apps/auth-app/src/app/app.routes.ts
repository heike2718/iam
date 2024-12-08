import { Route } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ForgotPasswordComponent } from '@auth-app/forgot-password/feature';
import { ChangeTempPasswordComponent } from '@auth-app/change-temp-password/feature';

export const appRoutes: Route[] = [

    {
		path: 'home',
		component: HomeComponent
	},
    {
		path: 'password/temp/order',
		component: ForgotPasswordComponent
	},
    {
		path: 'password/temp/change',
		component: ChangeTempPasswordComponent
	},
    {
        path: '**',
        component: HomeComponent
    }
];
