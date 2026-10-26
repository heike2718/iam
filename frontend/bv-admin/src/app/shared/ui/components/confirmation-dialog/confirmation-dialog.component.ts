import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
    selector: 'bv-admin-confirmation-dialog',
    imports: [MatButtonModule, MatDialogModule],
    templateUrl: './confirmation-dialog.component.html',
    styleUrls: ['./confirmation-dialog.component.scss'],
})
export class ConfirmationDialogComponent {
    public dialogRef = inject<MatDialogRef<ConfirmationDialogComponent>>(MatDialogRef);
    public data = inject<{ title: string; question: string }>(MAT_DIALOG_DATA);

    onNoClick(): void {
        this.dialogRef.close(false);
    }

    onYesClick(): void {
        this.dialogRef.close(true);
    }
}
