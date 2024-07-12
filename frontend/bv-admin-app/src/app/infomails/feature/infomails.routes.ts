import { inject } from "@angular/core";
import { Routes } from "@angular/router";
import { AuthFacade } from "@bv-admin-app/shared/auth/api";
import { InfomailsRootComponent } from "./infomails-root/infomails-root.component";
import { InfomailsListComponent } from "./infomails-list/infomails-list.component";


export const infomailsRoutes: Routes = [
    {
        path: '',
        canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
        component: InfomailsRootComponent,
        children: [
          {
            path: '',
            component: InfomailsListComponent,
          }
        //   {
        //     path: 'new',
        //     component: AddCustomerComponent,
        //     data: { mode: 'new' },
        //   },
        //   {
        //     path: ':id',
        //     component: EditCustomerComponent,
        //     data: { mode: 'edit' },
        //   },
        ],
      },
]