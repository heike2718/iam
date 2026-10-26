import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
    selector: 'lib-common-info-dialog',
    standalone: true,
    imports: [MatButtonModule, MatDialogModule],

    templateUrl: './info-dialog.component.html',
    styleUrls: ['./info-dialog.component.scss'],
})
export class InfoDialogComponent {
    public dialogRef = inject<MatDialogRef<InfoDialogComponent>>(MatDialogRef);
    public data = inject<{ title: string; text: string }>(MAT_DIALOG_DATA);

    onYesClick(): void {
        this.dialogRef.close(true);
    }
}
