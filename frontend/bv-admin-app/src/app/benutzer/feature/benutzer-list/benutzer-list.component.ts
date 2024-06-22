import { AsyncPipe, CommonModule, NgIf } from "@angular/common";
import { Component, OnInit, inject } from "@angular/core";
import { BenutzerFacade } from '@bv-admin-app/benutzer/api';
import { BenutzerTableFilter, PageDefinition } from '@bv-admin-app/benutzer/model';


@Component({
    selector: 'bv-users',
    standalone: true,
    imports: [
        CommonModule,
        NgIf,
        AsyncPipe
    ],
    templateUrl: './benutzer-list.component.html',
    styleUrls: ['./benutzer-list.component.scss'],
  })
  export class BenutzerListComponent implements OnInit {

    benutzerFacade = inject(BenutzerFacade);

    ngOnInit(): void {
      
      const filterValues: BenutzerTableFilter = {
        aktiviert: null,
        dateModified: null,
        email: null,
        nachname: null,
        rolle: 'ADMIN',
        uuid: null,
        vorname: null,
      }

      const pageDefinition: PageDefinition = {
        pageIndex: 1,
        pageSize: 10,
        sortByLabelname: 'vorname',
        sortDirection: 'asc'
      };

      this.benutzerFacade.findBenutzer(filterValues, pageDefinition);

    }

}