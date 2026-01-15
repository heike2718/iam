import { Component, EventEmitter, inject, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLinkWithHref } from '@angular/router';
import { ShellService } from '../../shell/shell.service';
import { AuthFacade } from '../../auth/api/auth.facade';
import { AsyncPipe } from '@angular/common';
import { BenutzerdatenFacade } from '../../benutzerdaten/api/benutzerdaten.facade';
import { anonymeBenutzerdaten, Benutzerdaten, fullName } from '../../benutzerdaten/model/benutzerdaten.model';

@Component({
    selector: 'app-benutzerprofil-toolbar',
    templateUrl: './toolbar.component.html',
    styleUrls: ['./toolbar.component.scss'],
    standalone: true,
    imports: [MatToolbarModule, MatIconModule, MatButtonModule, RouterLinkWithHref, AsyncPipe],
})
export class ToolbarComponent {
    authFacade = inject(AuthFacade);

    benutzer: Benutzerdaten = anonymeBenutzerdaten;
    benutzerdatenFacade = inject(BenutzerdatenFacade);

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

    getFullName(benuzerdaten: Benutzerdaten): string {
        return fullName(benuzerdaten);
    }
}
