import { AsyncPipe, CommonModule, NgIf } from "@angular/common";
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { BenutzerDataSource, BenutzerFacade } from '@bv-admin-app/benutzer/api';
import { Benutzer, BenutzersucheGUIModel } from '@bv-admin-app/benutzer/model';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort, SortDirection } from "@angular/material/sort";
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { Subscription, merge, tap } from "rxjs";
import { SelectionModel } from "@angular/cdk/collections";

const AUSWAHL_BENUTZER = 'auswahlBenutzer';
const UUID = 'uuid';
const EMAIL = 'email';
const NACHNAME = 'nachname';
const VORNAME = 'vorname';
const DATE_MODIFIED = 'dateModified';
const ROLLE = 'rolle';

@Component({
  selector: 'bv-users',
  standalone: true,
  imports: [
    CommonModule,
    NgIf,
    AsyncPipe,
    FormsModule,
    MatCheckboxModule,
    MatTableModule,
    MatSortModule,
    MatInputModule,
    MatPaginatorModule,
    MatButtonModule
  ],
  templateUrl: './benutzer-list.component.html',
  styleUrls: ['./benutzer-list.component.scss'],
})
export class BenutzerListComponent implements OnInit, OnDestroy, AfterViewInit {

  benutzerFacade = inject(BenutzerFacade);

  dataSource = inject(BenutzerDataSource);

  // Benutzer sollen ausgewählt werden können
  selection!: SelectionModel<Benutzer>;

  anzahlBenutzer = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  uuidFilterValue = '';
  emailFilterValue = '';
  vornameFilterValue = '';
  nachnameFilterValue = '';
  dateModifiedFilterValue = '';
  rolleFilterValue = '';

  #guiModel!: BenutzersucheGUIModel;
  


  #guiModelSubscription: Subscription = new Subscription();

  #pageSubscription = new Subscription();
  #matSortChangedSubscription: Subscription = new Subscription();
  #matPaginatorSubscription: Subscription = new Subscription();
  #paginationSortMergeSubscription: Subscription = new Subscription();

  #resetFilterDisabled = true;

  #adjusting = false;
  #triggerSearchImmediately = false;


  ngOnInit(): void {

    this.#guiModelSubscription = this.benutzerFacade.guiModel$.subscribe(
      (guiModel) => {
        if (!this.#adjusting) {
          this.#guiModel = guiModel;
          this.selection = new SelectionModel<Benutzer>(true, guiModel.selectedBenutzer);
        }
        this.#adjusting = false;
      }
    );

    this.#pageSubscription = this.benutzerFacade.page$.subscribe((page) => this.#resetFilterDisabled = page.length === 0);

  }

  ngAfterViewInit(): void {
    // fixes NG0100: Expression has changed after it was checked 
    // https://angular.io/errors/NG0100
    setTimeout(() => {

      // this.#initPaginator();
      // hier den init-Kram oder
    }, 0);

    this.#initPaginator();

    this.#paginationSortMergeSubscription = merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => {

        if (this.sort.direction) {
          const newSort = this.sort.direction === "asc" ? 'asc' : 'desc';
          this.#guiModel = { ...this.#guiModel, pageIndex: this.paginator.pageIndex, sortDirection: newSort };
        } else {
          this.#guiModel = { ...this.#guiModel, pageIndex: this.paginator.pageIndex, sortDirection: null, sortByLabelname: null };
        }
        

      })
    ).subscribe();

    this.#matPaginatorSubscription = merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => {
        this.sort.direction;
        this.#guiModel = { ...this.#guiModel, sortDirection: this.sort.direction, pageIndex: this.paginator.pageIndex, pageSize: this.paginator.pageSize };
        this.#triggerSearchImmediately = true;
        this.#guiModelChanged();
      })
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#guiModelSubscription.unsubscribe();
    this.#pageSubscription.unsubscribe();
    this.#matPaginatorSubscription.unsubscribe();
    this.#matSortChangedSubscription.unsubscribe();
    this.#paginationSortMergeSubscription.unsubscribe();
  }

  toggleRow(row: Benutzer) {
    const el = this.selection.selected.find((e) => e.uuid === row.uuid);
    el ? this.selection.deselect(el) : this.selection.select(row);

    const ben = this.#guiModel.selectedBenutzer.find((b) => b.uuid === row.uuid);
    let selectedNeu: Benutzer[] = [];
    if (ben) {
      selectedNeu = this.#guiModel.selectedBenutzer.filter(benutzer => benutzer.uuid === row.uuid);
    } else {
      selectedNeu = [...this.#guiModel.selectedBenutzer, row];
    }
    this.#guiModel = { ...this.#guiModel, selectedBenutzer: selectedNeu };
    this.#guiModelChanged();
  }

  applyFilter(column: string) {

    switch (column) {
      case 'uuid': this.#guiModel = { ...this.#guiModel, uuid: this.uuidFilterValue.trim() }; break;
      case 'email': this.#guiModel = { ...this.#guiModel, email: this.emailFilterValue.trim() }; break;
      case 'vorname': this.#guiModel = { ...this.#guiModel, vorname: this.vornameFilterValue.trim() }; break;
      case 'nachname': this.#guiModel = { ...this.#guiModel, nachname: this.nachnameFilterValue.trim() }; break;
      case 'dateModified': this.#guiModel = { ...this.#guiModel, dateModified: this.dateModifiedFilterValue.trim() }; break;
      case 'rolle': this.#guiModel = { ...this.#guiModel, rolle: this.rolleFilterValue.trim() }; break;
    }
    this.#guiModelChanged();
  }

  sortData(sort: Sort) {

    const sortDirection = sort.direction as 'asc' | 'desc';

    if (!sortDirection) {
      this.#guiModel = { ...this.#guiModel, sortByLabelname: sort.active, sortDirection: null }
    } else {
      this.#guiModel = { ...this.#guiModel, sortByLabelname: sort.active, sortDirection: sortDirection }
    }

    this.#triggerSearchImmediately = true;
    this.#guiModelChanged();

  }

  onPageChange(event: PageEvent) {

    this.#guiModel = { ...this.#guiModel, pageSize: event.pageSize, pageIndex: event.pageIndex };
    this.#triggerSearchImmediately = true;
    this.#guiModelChanged();
  }



  getDisplayedColumns(): string[] {
    return [AUSWAHL_BENUTZER, UUID, EMAIL, NACHNAME, VORNAME, DATE_MODIFIED, ROLLE];
  }

  findBenutzer() {
    this.benutzerFacade.startSuche(this.#guiModel);
  }

  resetFilterDisabled(): boolean {
    return this.#resetFilterDisabled;
  }

  resetFilter() {
    this.uuidFilterValue = '';
    this.emailFilterValue = '';
    this.nachnameFilterValue = '';
    this.vornameFilterValue = '';
    this.dateModifiedFilterValue = '';
    this.rolleFilterValue = '';
    this.benutzerFacade.resetFilterAndSort();
    this.#triggerSearchImmediately = false;
  }

  #initPaginator(): void {

    this.paginator.pageIndex = this.#guiModel.pageIndex;

    this.paginator._intl.itemsPerPageLabel = 'Einträge pro Seite';
    this.paginator._intl.nextPageLabel = 'nächste Seite';
    this.paginator._intl.previousPageLabel = 'vorherige Seite';
    this.paginator._intl.firstPageLabel = 'erste Seite';
    this.paginator._intl.lastPageLabel = 'letzte Seite';

    const originalGetRangeLabel = this.paginator._intl.getRangeLabel;
    this.paginator._intl.getRangeLabel = (
      page: number,
      pageSize: number,
      length: number
    ) => {
      return originalGetRangeLabel(page, pageSize, length).replace('of', 'von')
    }
    this.sort.direction = this.#guiModel.sortDirection === 'asc' ? 'asc' : 'desc';
    // reset Paginator when sort changed    
    this.#matSortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }

  #guiModelChanged(): void {
    this.#adjusting = true;
    this.benutzerFacade.guiModelChanged(this.#guiModel);

    if (this.#triggerSearchImmediately) {
      this.findBenutzer();
    }
  }
}
