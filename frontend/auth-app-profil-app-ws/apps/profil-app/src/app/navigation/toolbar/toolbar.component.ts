import { Component, EventEmitter, inject, Output } from "@angular/core";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLinkWithHref } from '@angular/router';
import { ShellService } from "../../shell/shell.service";

@Component({
    selector: 'profil-toolbar',
    templateUrl: './toolbar.component.html',
    styleUrls: ['./toolbar.component.scss'],
    standalone: true,
    imports: [
        MatToolbarModule,
        MatIconModule,
        MatButtonModule,
        RouterLinkWithHref
    ],
})
export class ToolbarComponent {

    @Output()
    sidenavToggle = new EventEmitter();

    shellService = inject(ShellService);

    onToggleSidenav(): void {
        this.sidenavToggle.emit();
    }
}