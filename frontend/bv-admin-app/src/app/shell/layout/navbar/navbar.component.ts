import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Router, RouterLinkWithHref } from '@angular/router';
import { map, shareReplay } from 'rxjs';

@Component({
  selector: 'bv-admin-header',
  standalone: true,
  imports: [
    CommonModule,
    MatMenuModule,
    MatIconModule,
    MatListModule,
    MatToolbarModule,
    MatTooltipModule,
    NgIf,
    RouterLinkWithHref
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {

  version = "1.0.0";

  @Output()
  sidenavToggle = new EventEmitter();

  #breakpointObserver = inject(BreakpointObserver);
  #router = inject(Router);

  isHandset$ = this.#breakpointObserver.observe(Breakpoints.Handset)
        .pipe(
            map(result => result.matches),
            shareReplay()
        );

  onToggleSidenav(): void {
    // console.log('onToggleSidenav');
    this.sidenavToggle.emit();
  }

  onMenuItemClick(id: number): void {
    this.#router.navigate(['/home', id]);
  }
}
