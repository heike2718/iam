import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormErrorComponent } from './form-error/form-error.component';
import { PasswordSpyComponent } from './password-spy/password-spy.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SwitchComponent } from './switch/switch.component';
import { DoublePasswordComponent } from './double-password/double-password.component';


@NgModule({
	imports: [
		CommonModule,
		ReactiveFormsModule,
		NgbModule
	],
	declarations: [
		DoublePasswordComponent,
		FormErrorComponent,
		PasswordSpyComponent,
		SwitchComponent
	],
	exports: [
		DoublePasswordComponent,
		FormErrorComponent,
		PasswordSpyComponent,
		SwitchComponent
	]
})
export class CommonComponentsModule { }
