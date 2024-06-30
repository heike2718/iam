import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, ViewChild, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { BenutzerDataSource, BenutzerFacade } from '@bv-admin-app/benutzer/api';
import { Benutzer, BenutzersucheFilterAndSortValues, initialBenutzersucheFilterAndSortValues, isFilterEmpty, BenutzersucheFilterValues } from '@bv-admin-app/benutzer/model';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort, SortDirection } from "@angular/material/sort";
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { Subscription, combineLatest, merge, of, tap } from "rxjs";
import { SelectionModel } from "@angular/cdk/collections";
import { PageDefinition, PaginationState, SortDefinition, initialPaginationState } from "@bv-admin-app/shared/model";

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

  // #matSortDirection: SortDirection = "";
  #paginationState: PaginationState = initialPaginationState;
  #page: Benutzer[] = [];
  #sortDefinition!: SortDefinition;
  #adjusting = false;

  // subscriptions

  #combinedBenutzerstoreSubscription = new Subscription();
  #matSortChangedSubscription: Subscription = new Subscription();

  constructor(private changeDetector: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {

    this.#combinedBenutzerstoreSubscription = combineLatest([
      this.#benutzerFacade.paginationState$,
      this.#benutzerFacade.benutzerBasket$,
      this.#benutzerFacade.filterValues$,
      this.#benutzerFacade.page$,
      this.#benutzerFacade.sortDefinition$
    ]).subscribe(
      ([
        paginationState,
        benutzerBasket,
        filterValues,
        page,
        sortDefinition
      ]) => {
        // wenn anzahlBenutzer nicht gesetzt wird, ist die Pagination inaktiv        
        this.anzahlBenutzer = paginationState.anzahlTreffer;
        this.#paginationState = paginationState;
        this.#initFilter(filterValues);
        this.#sortDefinition = sortDefinition;

        this.#page = page;
        this.selectionModel.clear();
        benutzerBasket
          .filter(selected => page.some(benutzer => benutzer.uuid === selected.uuid))
          .forEach(benutzer => this.selectionModel.select(benutzer));

        this.benutzerBasket = benutzerBasket;

        this.#initTableAndSort();
      }
    );

    // reset Paginator when sort changed    
    this.#matSortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    this.changeDetector.detectChanges();
  }

  ngOnDestroy(): void {
    this.#combinedBenutzerstoreSubscription.unsubscribe();
    this.#matSortChangedSubscription.unsubscribe();
  }

  toggleRow(row: Benutzer) {
    const el = this.selectionModel.selected.find((e) => e.uuid === row.uuid);
    el ? this.selectionModel.deselect(el) : this.selectionModel.select(row);

    const selected = this.#page.filter(benutzer => this.selectionModel.isSelected(benutzer));
    const deselected = this.#page.filter(benutzer => !this.selectionModel.isSelected(benutzer));
    this.#benutzerFacade.selectionsubsetChanged(selected, deselected);
  }

  sortData(sort: Sort) {

    if (this.#adjusting) {
      this.#adjusting = false;
      return;
    }

    const pageDefinition = { ...this.#createActualPageDefinition(), sortDirection: sort.direction.toString() };

    if (this.sucheDisabled()) {
      this.#benutzerFacade.benutzersucheChanged(this.#createActualFilterAndSort(), pageDefinition);
    } else {
      this.findBenutzer();
    }
  }

  getDisplayedColumns(): string[] {
    return [AUSWAHL_BENUTZER, UUID, EMAIL, NACHNAME, VORNAME, DATE_MODIFIED, ROLLE];
  }

  onPaginatorChanged(_event: PageEvent): void {
    this.#benutzerFacade.benutzersucheChanged(this.#createActualFilterAndSort(), this.#createActualPageDefinition());
  }

  findBenutzer() {
    const pageDefinition: PageDefinition = this.#createActualPageDefinition();
    const filter: BenutzersucheFilterAndSortValues = this.#createActualFilterAndSort();
    this.#benutzerFacade.triggerSearch(filter, pageDefinition);
  }

  sucheDisabled(): boolean {
    const actFilter = this.#createActualFilter();
    return isFilterEmpty(actFilter);
  }

  resetFilter() {
    // reset Paginator when filter is reseted
    this.paginator.pageIndex = 0;
    this.#initFilter(initialBenutzersucheFilterAndSortValues);
    this.#benutzerFacade.resetFilterAndSort();
  }

  #initFilter(filter: BenutzersucheFilterAndSortValues): void {

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

    // Apply the sort state
    this.sort.active = this.#sortDefinition.active;
    this.sort.direction = this.#sortDefinition.direction;
    this.#adjusting = true;

    // damit der Pfeil neben der Spalte auch den korrekten Status anzeigt, Angular nochmal sagen, dass er sich aktualisieren soll.
    this.sort.sortChange.emit({
      active: this.#sortDefinition.active,
      direction: this.#sortDefinition.direction
    });

    // console.log('sort.direction=' + this.sort.direction + ', sort.active=' + this.sort.active);

    this.paginator.pageSize = this.#paginationState.pageDefinition.pageSize;
    this.paginator.pageIndex = this.#paginationState.pageDefinition.pageIndex;

    this.changeDetector.detectChanges();
  }

  #createActualFilterAndSort(): BenutzersucheFilterAndSortValues {

    const matSortDirection = this.sort.direction;
    let sortByLabel = '';

    if (matSortDirection === "") {
      sortByLabel = '';
    } else {
      sortByLabel = this.sort.active;
    }

    const filter: BenutzersucheFilterAndSortValues = {
      aktiviert: null,
      dateModified: this.dateModifiedFilterValue,
      email: this.emailFilterValue,
      nachname: this.nachnameFilterValue,
      rolle: this.rolleFilterValue,
      sortByLabelname: sortByLabel,
      uuid: this.uuidFilterValue,
      vorname: this.vornameFilterValue
    };

    return filter;
  }

  #createActualFilter(): BenutzersucheFilterValues {
    return {
      dateModified: this.dateModifiedFilterValue,
      email: this.emailFilterValue,
      nachname: this.nachnameFilterValue,
      rolle: this.rolleFilterValue,
      uuid: this.uuidFilterValue,
      vorname: this.vornameFilterValue
    }
  }

  #createActualPageDefinition(): PageDefinition {

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : this.#paginationState.pageDefinition.pageIndex,
      pageSize: this.paginator ? this.paginator.pageSize : this.#paginationState.pageDefinition.pageSize,
      sortDirection: this.#paginationState.pageDefinition.sortDirection
    }

    return pageDefinition;
  }
}
