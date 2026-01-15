import { Route } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ForgotPasswordComponent } from './forgot-password/feature/forgot-password/forgot-password.component';
import { ChangeTempPasswordComponent } from './change-temp-password/feature/change-temp-password/change-temp-password.component';
import { LoginComponent } from './login-signup/feature/log-in/log-in.component';
import { SignUpComponent } from './login-signup/feature/sign-up/sign-up.component';

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
