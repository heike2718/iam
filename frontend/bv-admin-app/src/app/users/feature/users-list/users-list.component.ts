import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";


@Component({
    selector: 'bv-users',
    standalone: true,
    imports: [
        CommonModule
    ],
    templateUrl: './users-list.component.html',
    styleUrls: ['./users-list.component.scss'],
  })
  export class UsersListComponent {

}