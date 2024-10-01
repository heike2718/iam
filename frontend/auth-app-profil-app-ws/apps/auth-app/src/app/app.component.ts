import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MessageComponent, LoadingIndicatorComponent} from '@ap-ws/messages/ui';
import { HomeComponent } from './home/home.component';
import { AuthAppConfiguration } from './config/auth-app.configuration';
import { AuthFacade } from '@auth-app/auth/api';

@Component({
  standalone: true,
  imports: [
    MessageComponent,
    LoadingIndicatorComponent,
    HomeComponent,
    RouterModule],
  selector: 'auth-app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {

  configuration = inject(AuthAppConfiguration);

  imageSourceLogo = this.configuration.assetsPath + 'mja_logo_2.svg';

  #authFacade = inject(AuthFacade);

  ngOnInit(): void {

    this.#authFacade.createAnonymousSession();    
  }


}
