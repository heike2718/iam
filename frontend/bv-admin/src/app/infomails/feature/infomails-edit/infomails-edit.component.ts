import { CommonModule } from "@angular/common";
import { Component, inject, Input, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatInputModule } from "@angular/material/input";
import { InfomailsFacade } from "@bv-admin/infomails/api";
import { InfomailRequestDto } from "@bv-admin/infomails/model";
import { Infomail } from "@bv-admin/shared/model";


@Component({
    selector: 'bv-admin-infomail-edit',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatInputModule,
        MatButtonModule,
        MatCardModule
    ],
    templateUrl: './infomails-edit.component.html',
    styleUrls: ['./infomails-edit.component.scss'],
})
export class InfomailEditComponent implements OnInit {

    @Input()
    infomail: Infomail | undefined;

    infomailsFacade = inject(InfomailsFacade);

    infomailForm: FormGroup;

    constructor(private fb: FormBuilder) {
        this.infomailForm = this.fb.group({
            betreff: ['', [Validators.required, Validators.maxLength(100)]],
            mailtext: ['', Validators.required]
        });
    }

    ngOnInit(): void {
        this.#patchForm();
    }

    save(): void {
        if (this.infomailForm.valid) {
            const trimmedBetreff = this.infomailForm.get('betreff')?.value.trim();
            const trimmedMailtext = this.infomailForm.get('mailtext')?.value.trim();
            const requestDto: InfomailRequestDto = {
                betreff: trimmedBetreff,
                mailtext: trimmedMailtext
            };

            const uuid: string | undefined = this.infomail ? this.infomail.uuid : undefined;

            this.infomailsFacade.saveInfomailText(uuid, requestDto);
        }
    }


    cancelEdit(): void {
        this.infomailsFacade.cancelEdit();
    }

    onBlur(controlName: string): void {
        const control = this.infomailForm.get(controlName);
        if (control) {
            control.markAsTouched();
        }
    }



    #patchForm(): void {
        if (this.infomail) {
            this.infomailForm.patchValue(this.infomail);
        } else {
            this.infomailForm.patchValue({
                betreff: '',
                mailtext: ''
            });
        }
    }
}