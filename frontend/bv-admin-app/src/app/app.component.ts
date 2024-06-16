import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';

import { LoadingIndicatorComponent, MessageComponent } from './shared/messages/ui';
import { NavbarComponent } from './shell/layout/navbar/navbar.component';
import { SidenavComponent } from './shell/layout/sidenav/sidenav.component';

@Component({
  standalone: true,
  imports: [
    MatToolbarModule,
    MatSidenavModule,
    NavbarComponent,
    SidenavComponent,
    MessageComponent,
    LoadingIndicatorComponent,
    RouterModule
  ],
  selector: 'bv-admin-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {

  
}
