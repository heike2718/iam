import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NxWelcomeComponent } from './nx-welcome.component';
import { MessageComponent, LoadingIndicatorComponent} from '@auth-app-profil-app-ws/messages/ui';
import { LoadingService, MessageService } from '@auth-app-profil-app-ws/messages/api';

@Component({
  standalone: true,
  imports: [
    NxWelcomeComponent,
    MessageComponent,
    LoadingIndicatorComponent,
    RouterModule],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  title = 'auth-app';

  #messageService = inject(MessageService);
  #loadingService = inject(LoadingService);

  ngOnInit(): void {    
    this.#loadingService.start();
    this.#messageService.info('Das ist ein Willkommensgruß');
  }

}
