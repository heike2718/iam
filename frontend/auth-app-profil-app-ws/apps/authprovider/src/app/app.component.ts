import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MessageComponent, LoadingIndicatorComponent} from '@ap-ws/messages/ui';
import { HomeComponent } from './home/home.component';
import { AuthproviderConfiguration } from '@authprovider/configuration';

@Component({
  standalone: true,
  imports: [
    MessageComponent,
    LoadingIndicatorComponent,
    HomeComponent,
    RouterModule],
  selector: 'authprovider-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {

  configuration = inject(AuthproviderConfiguration);

  // imageSourceLogo = this.configuration.assetsPath + 'mja_logo_2.svg';
  imageSourceLogo = this.configuration.assetsPath + 'mja_logo_2-grau.svg';

  titel = '';

  #route = inject(ActivatedRoute);

  ngOnInit(): void {
    const state = this.#route.snapshot.queryParamMap.get('state');
    
    if (state && state === 'login') {
      this.titel = 'Einloggen';
    }

    if (state && state === 'signup') {
      this.titel = 'Registrieren';
    }
  }
  
}
