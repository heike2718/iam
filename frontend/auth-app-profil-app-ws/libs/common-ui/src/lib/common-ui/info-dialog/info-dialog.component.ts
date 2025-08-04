
import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
    selector: 'auth-common-info-dialog',
    standalone: true,
    imports: [
    MatButtonModule,
    MatDialogModule
],
    
    templateUrl: './info-dialog.component.html',
    styleUrls: ['./info-dialog.component.scss']
})
export class InfoDialogComponent {

    constructor(public dialogRef: MatDialogRef<InfoDialogComponent>, @Inject
        (MAT_DIALOG_DATA) public data: { title: string; text: string }) { }

    onYesClick(): void {
        this.dialogRef.close(true);
    }
}