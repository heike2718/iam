import { Component, OnInit, Input, Optional } from '@angular/core';
import { FormGroup, FormGroupDirective, NgForm } from '@angular/forms';

@Component({
	selector: 'cmn-form-error',
	template: `
		<small *ngIf="errorMessages" class="text-danger" aria-describedby="error">
			<div *ngFor="let errorMessage of errorMessages">
				{{errorMessage}}
			</div>
		</small>`
})
export class FormErrorComponent implements OnInit {

	@Input() path;
	@Input() text = '';


	constructor(@Optional() private ngForm: NgForm,
		@Optional() private formGroup: FormGroupDirective) { }

	ngOnInit() {
	}

	get errorMessages(): string[] {
		let form: FormGroup;

		if (this.ngForm) {
			form = this.ngForm.form;
		} else {
			form = this.formGroup.form;
		}
		const messages = [];
		const control = form.get(this.path);
		if (!control || !(control.touched) || !control.errors) {
			return null;
		}
		for (const code in control.errors) {
			if (control.errors.hasOwnProperty(code)) {
				const error = control.errors[code];
				let message = '';
				switch (code) {
					case 'requiredTrue':
						message = `Bitte stimmen Sie zu.`;
						break;
					case 'required':
						if (this.text === 'chckb') {
							message = `Bitte stimmen Sie zu.`;
						} else {
							message = `${this.text} ist ein Pflichtfeld`;
						}
						break;
					case 'minlength':
						message = `${this.text} muss mindestens ${error.requiredLength} Zeichen enthalten`;
						break;
					case 'maxlength':
						message = `${this.text} darf maximal ${error.requiredLength} Zeichen enthalten`;
						break;
					case 'email':
						message = `Bitte geben Sie eine gültige E-Mail Adresse an.`;
						break;
					case 'emailKnown':
						message = `Diese Mailadresse gibt es schon.`;
						break;
					case 'loginNameKnown':
						message = `Diesen Loginnamen gibt es schon.`;
						break;
					case 'invalidPassword':
						message = `Passwort-Regeln: mindestens 8 höchstens 20 Zeichen, mindestens ein Buchstabe, mindestens eine Ziffer,
 keine Leerzeichen am Anfang und am Ende`;
						break;
					case 'invalidEinmalPassword':
						message = `Das Einmalpasswort enthält ungültige Zeichen.`;
						break;
				}

				messages.push(message);
			}
		}

		return messages;
	}
}
