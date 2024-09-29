import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NxWelcomeComponent } from './nx-welcome.component';
import { MessageComponent} from '@auth-app-profil-app-ws/messages/ui';
import { MessageService } from '@auth-app-profil-app-ws/messages/api';

@Component({
  standalone: true,
  imports: [
    NxWelcomeComponent,
    MessageComponent,
    RouterModule],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  title = 'auth-app';

  #messageService = inject(MessageService);

  ngOnInit(): void {
    

    this.#messageService.warn('Das ist eine kleine Warnung als Willkommensgruß');
  }

}
