import { Component, Input, forwardRef, HostBinding, OnInit } from '@angular/core';
import { AbstractControl, ControlValueAccessor, FormControl, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { passwordValidator } from '@authprovider-ws/common-components';
import { calculateSpyTime } from '../commons-component.model';

@Component({
  selector: 'cmn-password',
  templateUrl: './password-spy.component.html',
  styleUrls: ['./password-spy.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PasswordSpyComponent),
      multi: true
    }
  ]
})
export class PasswordSpyComponent implements ControlValueAccessor, OnInit {

  showPassword = false;

  private ownId = '';

  passwordIsShown = false;

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

  public password!: AbstractControl;

  public passwordForm = new FormGroup({

    // dieser Name muss mit dem Attribut formControlName in der Elternkomponente übereinstimmen
    // und mit dem Attribut formControlName im input-Element.
    // Über diese Namensgleichkeit wird der value an die Elternkomponente weitergereicht.
    password: new FormControl('', [Validators.required, passwordValidator])
  });

  ngOnInit(): void {
    this.password = this.passwordForm.controls['password'];
  }

  public onTouched: () => void = () => {};

  // Override
  writeValue(val: any): void {
    val && this.passwordForm.setValue(val, { emitEvent: false });    
  }

  // Override
  registerOnChange(fn: any): void {
    this.passwordForm.valueChanges.subscribe(fn);
  }

  // Override
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  showPasswordDisabled(): boolean {
    if (this.passwordIsShown) {
      return true;
    }
    const val = this.passwordForm.controls['password'] ? this.passwordForm.controls['password'].value : undefined;
    return !val  || val.length === 0;
  }

  showThePassword() {
    this.showPassword = true;
    this.passwordIsShown = true;
    this.thePassword = this.passwordForm.controls['password'].value;
    
    const time = calculateSpyTime(this.thePassword);
    
    setTimeout(function() {
      this.passwordIsShown = false;
			this.showPassword = false;
      this.thePassword = '';
		}.bind(this), time);
  }
}
