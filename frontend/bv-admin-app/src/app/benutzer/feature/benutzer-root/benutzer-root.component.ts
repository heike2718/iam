import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";


@Component({
    templateUrl: './benutzer-root.component.html',
    standalone: true,
    imports: [RouterOutlet],
    selector: 'bv-admin-benutzer'  // selector wird von Angular u.a. verwendet, um eine eindeutige ID für de Komponente zu generieren
  })
  export class BenutzerRootComponent {

}