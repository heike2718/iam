import { AsyncPipe, CommonModule, NgIf } from "@angular/common";
import { Component, OnDestroy, OnInit, ViewChild, inject } from "@angular/core";
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BenutzerDataSource, BenutzerFacade } from '@bv-admin-app/benutzer/api';
import { BenutzerTableFilter, PageDefinition, PaginationState } from '@bv-admin-app/benutzer/model';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, SortDirection } from "@angular/material/sort";
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { Subscription } from "rxjs";


const NACHNAME = 'nachname';
const VORNAME = 'vorname';

@Component({
  selector: 'bv-users',
  standalone: true,
  imports: [
    CommonModule,
    NgIf,
    AsyncPipe,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatSortModule,
    MatInputModule,
    MatPaginatorModule,
    MatButtonModule
  ],
  templateUrl: './benutzer-list.component.html',
  styleUrls: ['./benutzer-list.component.scss'],
})
export class BenutzerListComponent implements OnInit, OnDestroy {

  benutzerFacade = inject(BenutzerFacade);

  dataSource = inject(BenutzerDataSource);

  anzahlBenutzer = 0;

  // @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  #paginationStateSubscription: Subscription = new Subscription();

  #paginationState!: PaginationState;
  #pageIndex = 0;
  #sortDirection: SortDirection = 'asc';
  #sortLabelName!: string;
  #adjusting = false;


  ngOnInit(): void {

    this.#paginationStateSubscription = this.benutzerFacade.paginationState$.subscribe(
      (paginationState) => {
        this.#paginationState = paginationState;
        this.#pageIndex = paginationState.pageDefinition.pageIndex;
        this.#sortDirection = paginationState.pageDefinition.sortDirection === 'asc' ? 'asc' : 'desc';
        this.#sortLabelName = paginationState.pageDefinition.sortByLabelname;
        this.anzahlBenutzer = paginationState.anzahlTreffer;
      }
    );
  }

  ngOnDestroy(): void {
    this.#paginationStateSubscription.unsubscribe();
  }

  applyFilter(event: Event, column: string) {
    const filterValue = (event.target as HTMLInputElement).value.trim();
    this.benutzerFacade.benutzerFilterChanged(column, filterValue);
  }

  getDisplayedColumns(): string[] {
    return [NACHNAME, VORNAME];
  }

  findBenutzer() {

    this.benutzerFacade.findBenutzer();

  }
}