import { AbstractControl, FormGroup, FormControl } from '@angular/forms';
import { TwoPasswords, trimString } from '@authprovider-ws/common-components';

// tslint:disable-next-line:max-line-length
const RE_PASSWORD = /(?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !&quot;#\$%&amp;'\(\)\*\+,\-\.\/:&lt;=&gt;\?@\[\]\^\\ _`'{|}~ ]{8,100}/;
const RE_EMAIL = /^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
export interface DoublePasswordsPayload {
	readonly firstPassword: string,
	readonly secondPassword: string
};

export function isEmpty(value: string) {
	
	return trimString(value).length === 0;
};

export function isValidPassword(value: string): boolean {

	if (!value) {
		return false;
	}

	const matches: boolean = RE_PASSWORD.test(value);

	const trimmed = trimString(value);
	if (trimmed !== value) {
		return false;
	}

	return matches;
};

export function isValidEmail(value?: string): boolean {

	if (!value) {
		return false;
	}

	const matches = RE_EMAIL.test(value);
	return matches;
}

export function passwordValidator(control: any): {
	[key: string]: any
} {

	const value = control.value;

	if (!isValidPassword(value)) {
		return { 'invalidPassword': true };
	}

	/*
	if (!control.value || control.value === '' || RE_PASSWORD.test(control.value)) {
		return null;
	} else {
		return { 'invalidPassword': true };
	}
	*/

	return null;
}

function areInputsEqual(val1: string, val2: string): boolean {

	if (!val1 && !val2) {
		return true;
	}

	if (!val1 && val2 || val1 && !val2) {
		return false;
	}

	return val1 === val2;
}

export function doublePasswordValidator(formGroup: AbstractControl): {
	[key: string]: any
} {
	const passwort1Control = formGroup.get('firstPassword');
	const passwort2Control = formGroup.get('secondPassword');

	if (!passwort1Control || !passwort2Control) {
		return null;
	}
	const passwort = passwort1Control.value;
	const passwortWdh = passwort2Control.value;

	if (!areInputsEqual(passwort, passwortWdh)) {
		return { 'passwortNeuPasswortNeuWdh': true };
	}

	return null;
}

export function inputsEqual(formGroup: FormGroup): {
	[key: string]: any
} {

	const controls: FormControl[] = [];
	Object.keys(formGroup.controls).forEach(field => {
		const control = formGroup.get(field);
		if (control instanceof FormControl) {
			controls.push(control);
		}
	});

	if (controls.length !== 2) {
		return null;
	}

	const control1 = controls[0];
	const control2 = controls[1];

	if (!control1 || !control2) {
		return null;
	}

	const val1 = extractTheValueAsString(control1);
	const val2 = extractTheValueAsString(control2);

	if (!areInputsEqual(val1, val2)) {
		return {'inputsDiffer': true}
	}	
}

export function isTwoPasswordsValid(payload: TwoPasswords): boolean {

	if (!payload || !payload.passwort || !payload.passwortWdh) {
		return false;
	}

	if (!isValidPassword(payload.passwort) || !isValidPassword(payload.passwortWdh)) {
		return false;
	}

	return payload.passwort === payload.passwortWdh;
}



export function validateAllFormFields(formGroup: FormGroup): void {
	Object.keys(formGroup.controls).forEach(field => {
		const control = formGroup.get(field);
		if (control instanceof FormControl) {
			control.markAsTouched({ onlySelf: true });
		} else if (control instanceof FormGroup) {
			validateAllFormFields(control);
		}
	});
};

// =============================  private functions =========================

function extractTheValueAsString(control: AbstractControl): string {


	if (control) {
		if (control.value) {
			if (control.value.value) {
				return control.value.value;
			} else {
				return control.value;
			}
		}
	}

	return '';
}
