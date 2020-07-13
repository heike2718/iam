import { AbstractControl, FormGroup, FormControl } from '@angular/forms';

export function passwortValidator(control: any): {
	[key: string]: any
} {
	// tslint:disable-next-line:max-line-length
	const re = /(?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !&quot;#\$%&amp;'\(\)\*\+,\-\.\/:&lt;=&gt;\?@\[\]\^\\ _`'{|}~ ]{8,100}/;

	if (!control.value || control.value === '' || re.test(control.value)) {
		return null;
	} else {
		return { invalidPassword: true };
	}
}

export function einmalPasswordValidator(control: any): {
	[key: string]: any
} {
	const re = /[a-zA-Z0-9\-]*/;

	if (!control.value || control.value === '' || re.test(control.value)) {
		return null;
	} else {
		return { invalidEinmalPassword: true };
	}
}

function hasPasswortPasswortWdhError(passwort1: string, passwort2: string): boolean {
	if (passwort1 !== undefined && passwort2 !== undefined && passwort1 !== '' && passwort2 !== '' && passwort1 !== passwort2) {
		return true;
	}
	return false;
}

function hasAttributeAttributeWdhEqualError(attr1: string, attr2: string): boolean {
	if (attr1 === undefined && attr2 === undefined) {
		return true;
	}
	if (attr1 === '' && attr2 === '') {
		return true;
	}
	if (attr1 !== undefined && attr1 !== '' && attr2 !== undefined && attr2 !== '' && attr1 === attr2) {
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
		return { passwortNeuPasswortNeuWdh: true };
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
		return { passwortNeuPasswortNeuWdh: true };
	}
}

export function emailEmailNeuValidator(formGroup: AbstractControl): {
	[key: string]: any
} {
	const email1Control = formGroup.get('username');
	const email2Control = formGroup.get('email');

	if (!email1Control || !email2Control) {
		return null;
	}
	const attr1 = email1Control.value;
	const attr2 = email2Control.value;

	if (hasAttributeAttributeWdhEqualError(attr1, attr2)) {
		return { attributeAttributeWdhEqual: true };
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

