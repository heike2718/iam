import { Component, EventEmitter, inject, Output } from "@angular/core";
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { RouterLinkWithHref } from '@angular/router';
import { ShellService } from "../../shell/shell.service";

@Component({
    selector: 'profil-sidenav',
    templateUrl: './sidenav.component.html',
    styleUrls: ['./sidenav.component.scss'],
    standalone: true,
    imports: [
        MatListModule,
        MatIconModule,
        RouterLinkWithHref
    ],
})
export class SidenavComponent {

    @Output()
    sidenavClose = new EventEmitter();

    shellService = inject(ShellService);

    public onSidenavClose = () => {
        this.sidenavClose.emit();
    }
}