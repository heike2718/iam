import { Route } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ForgotPasswordComponent } from '@authprovider/forgot-password/feature';
import { ChangeTempPasswordComponent } from '@authprovider/change-temp-password/feature';
import { LoginComponent, SignUpComponent } from '@authprovider/login-signup/feature';

export const appRoutes: Route[] = [

    {
		path: 'home',
		component: HomeComponent
	},	
	{

		path: 'login',
		component: LoginComponent
	},	
	{

		path: 'signup',
		component: SignUpComponent
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
