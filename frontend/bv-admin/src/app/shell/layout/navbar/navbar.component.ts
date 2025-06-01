import { Component, EventEmitter, OnDestroy, OnInit, Output, inject } from '@angular/core';
import { AsyncPipe, CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Router, RouterLinkWithHref } from '@angular/router';
import { Subscription, map, shareReplay } from 'rxjs';
import { AuthFacade } from '@bv-admin/shared/auth/api';
import { User } from '@bv-admin/shared/auth/model';

@Component({
    selector: 'bv-admin-header',
    imports: [
        CommonModule,
        MatMenuModule,
        MatIconModule,
        MatListModule,
        MatToolbarModule,
        MatTooltipModule,
        AsyncPipe,
        RouterLinkWithHref
    ],
    templateUrl: './navbar.component.html',
    styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit, OnDestroy {

  version = "1.1.0";

  @Output()
  sidenavToggle = new EventEmitter();

  authFacade = inject(AuthFacade);
  user!: User;

  #breakpointObserver = inject(BreakpointObserver);
  #router = inject(Router);

  #userSubscription = new Subscription();

 
  isHandset$ = this.#breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  ngOnInit(): void {

    this.#userSubscription = this.authFacade.user$.subscribe(
      (user) => this.user = user
    );

  }

  ngOnDestroy(): void {
    this.#userSubscription.unsubscribe();
  }

  login(): void {
    this.authFacade.login();
  }

  logout(): void {
    this.authFacade.logout();
  }

  onToggleSidenav(): void {
    // console.log('onToggleSidenav');
    this.sidenavToggle.emit();
  }

  onMenuItemClick(id: number): void {
    this.#router.navigate(['/home', id]);
  }
}
