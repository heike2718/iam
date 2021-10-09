import { Component, forwardRef, OnInit } from '@angular/core';
import { AbstractControl, ControlValueAccessor, FormControl, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { inputsEqual, PASSWORTREGELN, passwordValidator } from '@authprovider-ws/common-components';
import { calculateSpyTime } from '../commons-component.model';
import { DoublePasswordsPayload } from '../validation/app.validators';

@Component({
  selector: 'cmn-double-password',
  templateUrl: './double-password.component.html',
  styleUrls: ['./double-password.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DoublePasswordComponent),
      multi: true
    }
  ]
})
export class DoublePasswordComponent implements ControlValueAccessor, OnInit {

  public showPasswords: boolean[] = [false, false];
  public passwordsAreShown: boolean[] = [false, false];

  public tooltipPassword = PASSWORTREGELN;

  public thePasswords: string[] = ['', ''];

  private formControlNames: string[] = ['firstPassword', 'secondPassword'];

  public doublePasswordForm = new FormGroup({
    // diese Namen müssen mit den Attributen formControlName in der Elternkomponente übereinstimmen
    // und mit den Attributen formControlName im input-Element.
    // Über diese Namensgleichkeit wird der value an die Elternkomponente weitergereicht.
    'firstPassword': new FormControl('', [Validators.required, passwordValidator]),
    'secondPassword': new FormControl('', [Validators.required, passwordValidator])
  });

  public firstPassword!: AbstractControl;

  public secondPassword!: AbstractControl;

  constructor() {}

  ngOnInit(): void {

    if (!this.doublePasswordForm.hasValidator(inputsEqual)){
      this.doublePasswordForm.addValidators(inputsEqual);
    }

    this.firstPassword = this.doublePasswordForm.controls[this.formControlNames[0]];
    this.secondPassword = this.doublePasswordForm.controls[this.formControlNames[1]];
  }

  public onTouched: () => void = () => { };

  // Override
  writeValue(val: any): void {
    val && this.doublePasswordForm.setValue(val, { emitEvent: false });    
  }

  // Override
  registerOnChange(fn: any): void {
    this.doublePasswordForm.valueChanges.subscribe(fn);
  }

  // Override
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  showFirstPasswordDisabled(): boolean {
    if (this.passwordsAreShown[0]) {
      return true;
    }
    const formControlName = this.formControlNames[0];
    const val = this.doublePasswordForm.controls[formControlName] ? this.doublePasswordForm.controls[formControlName].value : undefined;
    return !val  || val.length === 0;
  }

  showSecondPasswordDisabled(): boolean {
    if (this.passwordsAreShown[1]) {
      return true;
    }
    const formControlName = this.formControlNames[1];
    const val = this.doublePasswordForm.controls[formControlName] ? this.doublePasswordForm.controls[formControlName].value : undefined;
    return !val  || val.length === 0;
  }

  showTheFirstPassword() {
    this.showPassword(0);    
  }

  showTheSecondPassword() {
    this.showPassword(1);    
  }

  private showPassword(index: number): void {
    this.showPasswords[index] = true;
    this.passwordsAreShown[index] = true;
    this.thePasswords[index] = this.doublePasswordForm.controls[this.formControlNames[index]].value;
    
    const time = calculateSpyTime(this.thePasswords[index]);
    
    setTimeout(function() {
      this.showPasswords[index] = false;
      this.passwordsAreShown[index] = false;
      this.thePasswords[index] = '';
		}.bind(this), time);
  }
}
