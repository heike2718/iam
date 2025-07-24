import { Component, inject } from '@angular/core';

import { LoadingService } from '@ap-ws/messages/api';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'

@Component({
  selector: 'auth-common-loader',
  standalone: true,
  imports: [MatProgressSpinnerModule],
  templateUrl: './loading-indicator.component.html',
  styleUrls: ['./loading-indicator.component.scss'],
})
export class LoadingIndicatorComponent {

  loadingService = inject(LoadingService);
}
