import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { BenutzerDataSource, BenutzerFacade } from '@bv-admin-app/benutzer/api';
import { Benutzer, BenutzersucheFilterValues, initialBenutzersucheFilterValues, isBenutzersucheFilterValuesEqual } from '@bv-admin-app/benutzer/model';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort, SortDirection } from "@angular/material/sort";
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { Subscription, combineLatest, merge, tap } from "rxjs";
import { SelectionModel } from "@angular/cdk/collections";
import { PageDefinition, PaginationState, initialPaginationState } from "@bv-admin-app/shared/model";

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
    NgFor,
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
export class BenutzerListComponent implements OnDestroy, AfterViewInit {


  dataSource = inject(BenutzerDataSource);

  // 2 way databind values
  uuidFilterValue = '';
  emailFilterValue = '';
  vornameFilterValue = '';
  nachnameFilterValue = '';
  dateModifiedFilterValue = '';
  rolleFilterValue = '';

  // Benutzer sollen ausgewählt werden können
  selectionModel: SelectionModel<Benutzer> = new SelectionModel<Benutzer>(true, []);
  benutzerBasket: Benutzer[] = [];

  anzahlBenutzer = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  #benutzerFacade = inject(BenutzerFacade);

  #matSortDirection: SortDirection = "";
  #sortByLabelName = '';
  #paginationState: PaginationState = initialPaginationState;

  #page: Benutzer[] = [];

  // subscriptions

  #combinedBenutzerstoreSubscription = new Subscription();
  #matPaginatorSubscription: Subscription = new Subscription();
  #matSortChangedSubscription: Subscription = new Subscription();

  #resetFilterDisabled = true;

  constructor(private changeDetector: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {

    // TODO #sortByLabelName initalisieren, filterValues initialisieren, sortDirection initialisieren

    this.#combinedBenutzerstoreSubscription = combineLatest([
      this.#benutzerFacade.paginationState$,
      this.#benutzerFacade.benutzerBasket$,
      this.#benutzerFacade.filterValues$,
      this.#benutzerFacade.page$
    ]).subscribe(
      ([
        paginationState,
        benutzerBasket,
        filterValues,
        page
      ]) => {
        // wenn anzahlBenutzer nicht gesetzt wird, ist die Pagination inaktiv
        this.anzahlBenutzer = paginationState.anzahlTreffer;
        this.#paginationState = paginationState;
        this.#initFilter(filterValues);

        this.#page = page;
        this.selectionModel.clear();
        benutzerBasket
          .filter(selected => page.some(benutzer => benutzer.uuid === selected.uuid))
          .forEach(benutzer => this.selectionModel.select(benutzer));

        this.benutzerBasket = benutzerBasket;
      }
    );

    this.#initTableAndSort();

    // wegen NG0100: ExpressionChangedAfterItHasBeenCheckedError nach rücknavigation explizit nochmal changeDetection triggern
    this.changeDetector.detectChanges();
  }

  ngOnDestroy(): void {
    this.#combinedBenutzerstoreSubscription.unsubscribe();
    this.#matPaginatorSubscription.unsubscribe();
    this.#matSortChangedSubscription.unsubscribe()
  }

  toggleRow(row: Benutzer) {
    const el = this.selectionModel.selected.find((e) => e.uuid === row.uuid);
    el ? this.selectionModel.deselect(el) : this.selectionModel.select(row);

    const selected = this.#page.filter(benutzer => this.selectionModel.isSelected(benutzer));
    const deselected = this.#page.filter(benutzer => !this.selectionModel.isSelected(benutzer));
    this.#benutzerFacade.selectionsubsetChanged(selected, deselected);
  }

  sortData(sort: Sort) {

    this.#matSortDirection = sort.direction;
    if (this.#matSortDirection === "") {
      this.#sortByLabelName = '';
    } else {
      this.#sortByLabelName = sort.active;
    }

    this.findBenutzer();
  }

  getDisplayedColumns(): string[] {
    return [AUSWAHL_BENUTZER, UUID, EMAIL, NACHNAME, VORNAME, DATE_MODIFIED, ROLLE];
  }

  findBenutzer() {
    const pageDefinition: PageDefinition = this.#createActualPageDefinition();
    const filter: BenutzersucheFilterValues = this.#createActualFilter();
    this.#benutzerFacade.triggerSearch(filter, pageDefinition);
  }

  updateResetFilterButtonState(): void {
    const actualFilterValues: BenutzersucheFilterValues = this.#createActualFilter();
    if (!isBenutzersucheFilterValuesEqual(initialBenutzersucheFilterValues, actualFilterValues)) {
      this.#resetFilterDisabled = false;
    } else {
      this.#resetFilterDisabled = true;
    }
  }

  resetFilterDisabled(): boolean {
    return this.#resetFilterDisabled;
  }

  resetFilter() {
    // reset Paginator when sort changed    
    this.#matSortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    this.#initFilter(initialBenutzersucheFilterValues);
    this.#benutzerFacade.resetFilterAndSort();
    this.#resetFilterDisabled = true;
  }

  #initFilter(filter: BenutzersucheFilterValues): void {

    this.uuidFilterValue = filter.uuid;
    this.emailFilterValue = filter.email;
    this.nachnameFilterValue = filter.nachname;
    this.vornameFilterValue = filter.vorname;
    this.dateModifiedFilterValue = filter.dateModified;
    this.rolleFilterValue = filter.rolle;
  }

  #initTableAndSort(): void {

    this.paginator.pageIndex = this.#paginationState.pageDefinition.pageIndex;


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

    this.sort.active = this.#sortByLabelName;
    this.sort.direction = this.#matSortDirection;
    // reset Paginator when sort changed    
    this.#matSortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }

  #createActualFilter(): BenutzersucheFilterValues {

    const filter: BenutzersucheFilterValues = {
      aktiviert: null,
      dateModified: this.dateModifiedFilterValue,
      email: this.emailFilterValue,
      nachname: this.nachnameFilterValue,
      rolle: this.rolleFilterValue,
      sortByLabelname: this.#sortByLabelName,
      uuid: this.uuidFilterValue,
      vorname: this.vornameFilterValue
    };

    return filter;
  }

  #createActualPageDefinition(): PageDefinition {

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : this.#paginationState.pageDefinition.pageIndex,
      pageSize: this.paginator ? this.paginator.pageSize : this.#paginationState.pageDefinition.pageSize,
      sortDirection: this.#matSortDirection.toString()
    }

    return pageDefinition;
  }
}
