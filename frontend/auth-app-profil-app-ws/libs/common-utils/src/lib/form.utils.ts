import { FormGroup, AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";


export function trimFormValues(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
        const control = formGroup.get(key);
        if (control && typeof control.value === 'string') {
            control.setValue(control.value.trim());
        }
    });
}

export function forbiddenPasswordValidator(pattern: RegExp): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) return null; // Ignore empty values
    const forbidden = !pattern.test(control.value);
    return forbidden ? { forbiddenPassword: { value: control.value } } : null;
  };
}

export function passwordsMatchValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const passwortNeu = control.get('passwortNeu')?.value;
    const passwortNeuWdh = control.get('passwortNeuWdh')?.value;

    // console.log('passwortNeu=' + passwortNeu + ', passwortNeuWdh=' + passwortNeuWdh);
    return passwortNeu && passwortNeuWdh && passwortNeu !== passwortNeuWdh
      ? { passwordsMismatch: true }
      : null;
  };
}
