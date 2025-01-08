import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
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
export class AppComponent  {

  configuration = inject(AuthproviderConfiguration);

  imageSourceLogo = this.configuration.assetsPath + 'mja_logo_2.svg';
  
}
