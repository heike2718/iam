import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { MessageComponent, LoadingIndicatorComponent } from '@auth-app-profil-app-ws/messages/ui';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CommonModule } from '@angular/common';
import { SidenavComponent } from '../navigation/sidenav/sidenav.component';
import { ToolbarComponent } from '../navigation/toolbar/toolbar.component';
import { Subscription, tap } from 'rxjs';
import { ShellService } from './shell.service';
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';


@Component({
    selector: 'profil-shell',
    templateUrl: './shell.component.html',
    standalone: true,
    styleUrl: './shell.component.scss',
    imports: [
        CommonModule,
        RouterModule,
        RouterOutlet,
        MessageComponent,
        LoadingIndicatorComponent,
        SidenavComponent,
        ToolbarComponent,
        MatSidenavModule,
        MatToolbarModule
    ]
})
export class ShellComponent implements OnInit, OnDestroy {

    #breakpointObserver = inject(BreakpointObserver);
    #subscription: Subscription = new Subscription();

    shellService = inject(ShellService);

    ngOnInit(): void {
        this.#subscription = this.#breakpointObserver.observe(Breakpoints.Handset).pipe(
            tap((state: BreakpointState) => {
                if (state.matches) {
                    this.shellService.setHandset(true);
                } else {
                    this.shellService.setHandset(false);
                }
            })
        ).subscribe();
    }

    ngOnDestroy(): void {
        this.#subscription.unsubscribe();
    }
}