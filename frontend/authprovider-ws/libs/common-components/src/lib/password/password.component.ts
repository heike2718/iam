import { Component, Input, forwardRef, HostBinding, OnInit } from '@angular/core';
import { AbstractControl, ControlValueAccessor, FormControl, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { passwortValidator } from '@authprovider-ws/common-components';

@Component({
  selector: 'cmn-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PasswordComponent),
      multi: true
    }
  ]
})
export class PasswordComponent implements ControlValueAccessor, OnInit {

  showPassword = false;

  private ownId = '';

  // referenz auf eine möglicherweise von der parent-component gesetzte id.
  @HostBinding('attr.id')
  externalId = '';

  @Input()
  set id(value: string) {
    this.ownId = value;

    // setzt die extern gesetzte id des Elements null, damit es nicht 2 ids hat.
    this.externalId = null;
  }

  get id() {
    return this.ownId;
  }

  public thePassword = '';

  public passwort!: AbstractControl;

  public passwortForm = new FormGroup({
    passwort: new FormControl('', [Validators.required, passwortValidator])
  });

  ngOnInit(): void {
    this.passwort = this.passwortForm.controls['passwort'];
  }

  public onTouched: () => void = () => {};

  // Override
  writeValue(val: any): void {
    val && this.passwortForm.setValue(val, { emitEvent: false });    
  }

  // Override
  registerOnChange(fn: any): void {
    console.log("on change");
    this.passwortForm.valueChanges.subscribe(fn);
  }

  // Override
  registerOnTouched(fn: any): void {
    console.log("on blur");
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.passwortForm.disable() : this.passwortForm.enable();
  }

  showPasswordDisabled(): boolean {
    const val = this.passwortForm.controls['passwort'] ? this.passwortForm.controls['passwort'].value : undefined;
    return !val  || val.length === 0;
  }

  showThePassword() {
    this.showPassword = true;
    this.thePassword = this.passwortForm.controls['passwort'].value;
    console.log('value=' + this.thePassword);
    setTimeout(function() {
			this.showPassword = false;
      this.thePassword = '';
		}.bind(this), 5000);
  }


}
