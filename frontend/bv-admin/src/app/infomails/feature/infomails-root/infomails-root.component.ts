import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";

@Component({
    templateUrl: './infomails-root.component.html',
    standalone: true,
    imports: [RouterOutlet],
    selector: 'bv-admin-infomails'  // selector wird von Angular u.a. verwendet, um eine eindeutige ID für de Komponente zu generieren
  })
export class InfomailsRootComponent {

}