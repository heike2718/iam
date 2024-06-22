import { inject } from "@angular/core";
import { Routes } from "@angular/router";
import { AuthFacade } from "@bv-admin-app/shared/auth/api";
import { UsersRootComponent } from "./users-root/users-root.component";
import { UsersListComponent } from "./users-list/users-list.component";


export const usersRoutes: Routes = [
    {
        path: '',
        canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
        component: UsersRootComponent,
        children: [
          {
            path: '',
            component: UsersListComponent,
          },
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