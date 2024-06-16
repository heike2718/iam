import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NxWelcomeComponent } from './nx-welcome.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SidenavComponent, NavbarComponent } from '@bv-admin-app/shell/layout';

@Component({
  standalone: true,
  imports: [
    MatToolbarModule,
    MatSidenavModule,
    NavbarComponent,
    SidenavComponent,
    NxWelcomeComponent,
    RouterModule
  ],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'bv-admin-app';
}
