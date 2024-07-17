import { inject } from "@angular/core";
import { Routes } from "@angular/router";
import { AuthFacade } from "@bv-admin-app/shared/auth/api";
import { VersandauftraegeRootComponent } from "./versandauftraege-root/versandauftraege-root.component";
import { VersandauftraegeListComponent } from "./versandauftraege-list/versandauftraege-list.component";
import { VersandauftragDetailsComponent } from "./versandauftrag-details/versandauftrag-details.component";


export const versandauftraegeRoutes: Routes = [
  {
    path: '',
    canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
    component: VersandauftraegeRootComponent,
    children: [
      {
        path: '',
        component: VersandauftraegeListComponent,
      },
      {
        path: 'details',
        component: VersandauftragDetailsComponent
      }
    ],
  },
]