import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoadingService } from '@auth-app-profil-app-ws/messages/api';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'

@Component({
  selector: 'auth-loader',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule],
  templateUrl: './loading-indicator.component.html',
  styleUrls: ['./loading-indicator.component.scss'],
})
export class LoadingIndicatorComponent {

  loadingService = inject(LoadingService);
}
