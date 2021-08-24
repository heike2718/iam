import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { HewiNgLibModule } from 'hewi-ng-lib';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { routerConfig } from './router.config';
import { HomeComponent } from './home/home.component';
import { LoggedInGuard } from './shared/logged-in.guard';
import { GlobalErrorHandlerService } from './error/global-error-handler.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProfilComponent } from './profil/profil.component';
import { BaseDataComponent } from './profil/base-data.component';
import { PasswordComponent } from './profil/password.component';
import { FormErrorComponent } from './shared/components/form-error/form-error.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { DeleteAccountComponent } from './profil/delete-account.component';
import { AboutComponent } from './about/about.component';

@NgModule({
	declarations: [
		AppComponent,
		NavbarComponent,
		HomeComponent,
		ProfilComponent,
		BaseDataComponent,
		PasswordComponent,
		FormErrorComponent,
		DeleteAccountComponent,
		AboutComponent,
	],
	imports: [
		BrowserModule,
		RouterModule.forRoot(routerConfig, { useHash: true, relativeLinkResolution: 'legacy' }),
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule,
		NgbModule,
		NgbCollapseModule,
		HewiNgLibModule
	],
	providers: [
		GlobalErrorHandlerService,
		{ provide: ErrorHandler, useClass: GlobalErrorHandlerService },
		{
			provide: HTTP_INTERCEPTORS,
			useClass: AuthInterceptor,
			multi: true
		},
		LoggedInGuard
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
