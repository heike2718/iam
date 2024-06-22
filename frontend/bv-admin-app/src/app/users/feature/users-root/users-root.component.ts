import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";


@Component({
    templateUrl: './users-root.component.html',
    standalone: true,
    imports: [RouterOutlet],
    selector: 'bv-admin-users'  // selector wird von Angular u.a. verwendet, um eine eindeutige ID für de Komponente zu generieren
  })
  export class UsersRootComponent {

}