import { Component, inject } from '@angular/core';

import { LoadingService } from '@bv-admin/shared/messages/api';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'

@Component({
    selector: 'bv-admin-loader',
    imports: [MatProgressSpinnerModule],
    templateUrl: './loading-indicator.component.html',
    styleUrls: ['./loading-indicator.component.scss']
})
export class LoadingIndicatorComponent {

  loadingService = inject(LoadingService);
}
