import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";

@Component({
    templateUrl: './versandauftraege-root.component.html',
    imports: [RouterOutlet],
    selector: 'bv-admin-versandauftraege' // selector wird von Angular u.a. verwendet, um eine eindeutige ID für die Komponente zu generieren
})
export class VersandauftraegeRootComponent {

}