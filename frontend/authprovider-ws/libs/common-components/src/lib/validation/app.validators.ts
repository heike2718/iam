import { AbstractControl, FormGroup, FormControl } from '@angular/forms';

export interface DoublePasswordsPayload {
	readonly firstPassword: string,
	readonly secondPassword: string
};

export function isEmpty(value: string) {
	if (!value) {
		return true;
	}

	return  value.length === 0;
};

export function isValidPassword(value: string): boolean {

	if (!value) {
		return true;
	}

	if (isEmpty(value)) {
		return false;
	}

	const re = /(?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !&quot;#\$%&amp;'\(\)\*\+,\-\.\/:&lt;=&gt;\?@\[\]\^\\ _`'{|}~ ]{8,100}/;

	const matches: boolean = re.test(value);
	return matches;
};

export function emailValidator(control: AbstractControl): {
	[key: string]: any
} | null {

	const theValue: string = extractTheValueAsString(control);

	if (theValue === '') {
		return null;
	}

	const re = /^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$/;
	if (re.test(theValue)) {
		return null;
	} else {
		return { 'invalidEMail': true };
	}

};

export function passwordValidator(control: any): {
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

function areInputsEqual(val1: string, val2: string): boolean {

	if (!val1 && !val2) {
		return true;
	}

	if (!val1 && val2 || val1 && !val2) {
		return false;
	}

	return val1 === val2;
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
	const passwort1Control = formGroup.get('password');
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
	if (hasPasswortPasswortWdhError(passwort, passwortWdh)) {
		return { 'passwortNeuPasswortNeuWdh': true };
	}
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
