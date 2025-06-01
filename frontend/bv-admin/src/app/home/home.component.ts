import { AsyncPipe, CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { AuthFacade } from "@bv-admin/shared/auth/api";


@Component({
    selector: 'bv-admin-home',
    imports: [
        CommonModule,
        AsyncPipe
    ],
    templateUrl: './home.component.html'
})
export class HomeComponent {

    authFacade = inject(AuthFacade);

}