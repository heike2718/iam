import { Component, EventEmitter, inject, Output } from "@angular/core";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLinkWithHref } from '@angular/router';
import { ShellService } from "../../shell/shell.service";
import { AuthFacade } from "@profil-app/auth/api";
import { AsyncPipe } from "@angular/common";

@Component({
    selector: 'profil-toolbar',
    templateUrl: './toolbar.component.html',
    styleUrls: ['./toolbar.component.scss'],
    standalone: true,
    imports: [
        MatToolbarModule,
        MatIconModule,
        MatButtonModule,
        RouterLinkWithHref,
        AsyncPipe
    ],
})
export class ToolbarComponent {

    authFacade = inject(AuthFacade);

    @Output()
    sidenavToggle = new EventEmitter();

    shellService = inject(ShellService);    

    onToggleSidenav(): void {
        this.sidenavToggle.emit();
    }

    login(): void {
        this.authFacade.login();
    }

    logout(): void {

        this.authFacade.logout();

    }
}