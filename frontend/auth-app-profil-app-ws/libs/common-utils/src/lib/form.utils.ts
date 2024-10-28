import { FormGroup } from "@angular/forms";


export function trimFormValues(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
        const control = formGroup.get(key);
        if (control && typeof control.value === 'string') {
            control.setValue(control.value.trim());
        }
    });
}
