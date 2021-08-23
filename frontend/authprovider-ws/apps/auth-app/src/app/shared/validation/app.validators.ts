import { AbstractControl, FormGroup, FormControl, AsyncValidatorFn } from '@angular/forms';
import { Subject, Observable, Observer } from 'rxjs';

export function emailValidator(control: any): {
	[key: string]: any
} {
	// tslint:disable-next-line:max-line-length
	const re = /^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$/;

	if (!control.value || control.value === '' || re.test(control.value)) {
		return null;
	} else {
		return { invalidEmail: true };
	}
}

export function passwortValidator(control: any): {
	[key: string]: any
} {
	// tslint:disable-next-line:max-line-length
	const re = /(?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !&quot;#\$%&amp;'\(\)\*\+,\-\.\/:&lt;=&gt;\?@\[\]\^\\ _`'{|}~ ]{8,100}/;

	if (!control.value || control.value === '' || re.test(control.value)) {
		return null;
	} else {
		return { 'invalidPassword': true };
	}
}

function hasPasswortPasswortWdhError(passwort1: string, passwort2: string): boolean {
	if (passwort1 !== undefined && passwort2 !== undefined && passwort1 !== '' && passwort2 !== '' && passwort1 !== passwort2) {
		return true;
	}
	return false;
}

export function passwortPasswortNeuValidator(formGroup: AbstractControl): {
	[key: string]: any
} {
	const passwort1Control = formGroup.get('passwortNeu');
	const passwort2Control = formGroup.get('passwortNeuWdh');

	if (!passwort1Control || !passwort2Control) {
		return null;
	}
	const passwort1 = passwort1Control.value;
	const passwort2 = passwort2Control.value;

	if (hasPasswortPasswortWdhError(passwort1, passwort2)) {
		return { 'passwortNeuPasswortNeuWdh': true };
	}
}

export function passwortPasswortWiederholtValidator(formGroup: AbstractControl): {
	[key: string]: any
} {
	const passwort1Control = formGroup.get('passwort');
	const passwort2Control = formGroup.get('passwortWdh');

	if (!passwort1Control || !passwort2Control) {
		return null;
	}
	const passwort = passwort1Control.value;
	const passwortWdh = passwort2Control.value;
	if (hasPasswortPasswortWdhError(passwort, passwortWdh)) {
		return { 'passwortNeuPasswortNeuWdh': true };
	}
}

export function validateAllFormFields(formGroup: FormGroup): void {
	Object.keys(formGroup.controls).forEach(field => {
		const control = formGroup.get(field);
		if (control instanceof FormControl) {
			control.markAsTouched({ onlySelf: true });
		} else if (control instanceof FormGroup) {
			this.validateAllFormFields(control);
		}
	});
}
