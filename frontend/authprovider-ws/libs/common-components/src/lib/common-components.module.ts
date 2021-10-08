import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormErrorComponent } from './form-error/form-error.component';
import { PasswordComponent } from './password/password.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SwitchComponent } from './switch/switch.component';


@NgModule({
	imports: [
		CommonModule,
		ReactiveFormsModule
	],
	declarations: [
		FormErrorComponent,
		PasswordComponent,
		SwitchComponent
	],
	exports: [
		FormErrorComponent,
		PasswordComponent,
		SwitchComponent
	]
})
export class CommonComponentsModule { }
