import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { AuthFacade } from "@profil-app/auth/api";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { trimFormValues } from "@ap-ws/common-utils";
import { ZweiPassworte } from "@ap-ws/common-model";
import { ZweiPassworteComponent } from "@ap-ws/common-ui";


@Component({
    selector: 'profil-passwort',
    templateUrl: './change-passwort.component.html',
    styleUrl: './change-passwort.component.scss',
    standalone: true,
    imports: [
      CommonModule,
      ReactiveFormsModule,
      MatButtonModule,
      MatInputModule,
      AsyncPipe,
      ZweiPassworteComponent
    ]
  })
  export class ChangePasswortComponent implements OnInit, OnDestroy {

    authFacade = inject(AuthFacade);

    parentForm!: FormGroup;

    #fb = new FormBuilder();

    ngOnInit(): void {
      this.parentForm = this.#fb.group({
        passwort: ['', Validators.required]
      });
        
    }

    ngOnDestroy(): void {
        
    }

    buttonSubmitDisabled(): boolean {
      return false;
    }

    buttonCancelDisabled(): boolean {
      return false;
    }

    handlePasswordChanges(zweiPassworte: ZweiPassworte): void {
      console.log('Password changes received:', {
        ...this.parentForm.value,
        ...zweiPassworte
      });
      // You can add backend saving logic here as needed
    }

    save(): void{

    }

    cancel(): void {
      
    }

}