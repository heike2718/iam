import { Route } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ForgotPasswordComponent } from '@auth-app/forgot-password/feature';

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
        path: '**',
        component: HomeComponent
    }
];
