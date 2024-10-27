import { Component, EventEmitter, inject, Output } from "@angular/core";
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { RouterLinkWithHref } from '@angular/router';
import { ShellService } from "../../shell/shell.service";
import { AuthFacade } from "@profil-app/auth/api";
import { AsyncPipe } from "@angular/common";

@Component({
    selector: 'profil-sidenav',
    templateUrl: './sidenav.component.html',
    styleUrls: ['./sidenav.component.scss'],
    standalone: true,
    imports: [
        MatListModule,
        MatIconModule,
        RouterLinkWithHref,
        AsyncPipe
    ],
})
export class SidenavComponent {

    @Output()
    sidenavClose = new EventEmitter();

    shellService = inject(ShellService);
    authFacade = inject(AuthFacade);

    public onSidenavClose = () => {
        this.sidenavClose.emit();
    }

    login(): void {
        this.authFacade.login();
    }

    logout(): void {
        this.authFacade.logout();
    }
}