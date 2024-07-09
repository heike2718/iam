import { AsyncPipe, CommonModule, NgFor, NgIf } from "@angular/common";
import { AfterViewInit, ChangeDetectorRef, Component, Inject, OnDestroy, ViewChild, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { BenutzerDataSource, BenutzerFacade } from '@bv-admin-app/benutzer/api';
import { Benutzer, BenutzersucheFilterAndSortValues, initialBenutzersucheFilterAndSortValues, isFilterEmpty, BenutzersucheFilterValues, isFilterAndSortEmpty } from '@bv-admin-app/benutzer/model';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from "@angular/material/sort";
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { Subscription, combineLatest } from "rxjs";
import { SelectionModel } from "@angular/cdk/collections";
import { PageDefinition, PaginationState, SortDefinition, initialPaginationState } from "@bv-admin-app/shared/model";
import { Router } from "@angular/router";
import { MatIconModule } from "@angular/material/icon";
import { MatDialog } from "@angular/material/dialog";
import { ConfirmationDialogComponent } from "@bv-admin-app/shared/ui";

const AUSWAHL_BENUTZER = 'auswahlBenutzer';
const UUID = 'uuid';
const EMAIL = 'email';
const NACHNAME = 'nachname';
const VORNAME = 'vorname';
const DATE_MODIFIED = 'dateModified';
const ROLLE = 'rolle';
const DELETE_BENUTZER_ACTION = "deleteAccount";
const TOGGLE_ACTIVATION_STATE_ACTION = "toggleActivationState"

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
    MatButtonModule,
    MatIconModule
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
  allRowsSelected = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  benutzerFacade = inject(BenutzerFacade);

  #router = inject(Router);
  #paginationState: PaginationState = initialPaginationState;
  #page: Benutzer[] = [];
  #adjusting = false;

  // eine für alle mit add :)
  #subscriptions: Subscription = new Subscription();

  constructor(private changeDetector: ChangeDetectorRef, public confirmDeleteDialog: MatDialog) {
  }

  ngAfterViewInit(): void {

    const combinedBenutzerstoreSubscription = combineLatest([
      this.benutzerFacade.paginationState$,
      this.benutzerFacade.filterValues$,
      this.benutzerFacade.sortDefinition$,
      this.benutzerFacade.page$,
      this.benutzerFacade.benutzerBasket$
    ]).subscribe(
      ([
        paginationState,
        filterValues,
        sortDefinition,
        page,
        benutzerBasket
      ]) => {

        this.#applyInitialState(paginationState, filterValues, sortDefinition, page, benutzerBasket);
        this.#adjusting = false;
      }
    );
    combinedBenutzerstoreSubscription.unsubscribe();

    this.#registerSubscribers();
  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
  }

  deleteBenutzer(benutzer: Benutzer): void {

    const dialogRef = this.confirmDeleteDialog.open(ConfirmationDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Benutzer löschen',
        question: 'Bist Du vollkommen sicher, dass Du das Benutzerkonto von "' + benutzer.vorname + ' ' + benutzer.nachname + '" löschen willst?'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.#doDeleteBenuzuer(benutzer);
      }
    });
  }

  changeActivationState(benutzer: Benutzer): void {
    if (benutzer.aktiviert) {
      this.benutzerFacade.updateBenutzerAktivierungsstatue(benutzer, false);
    } else {
      this.benutzerFacade.updateBenutzerAktivierungsstatue(benutzer, true);
    }
  }

  toggleRow(row: Benutzer) {

    if (this.#adjusting) {
      return;
    }

    const el = this.selectionModel.selected.find((e) => e.uuid === row.uuid);
    el ? this.selectionModel.deselect(el) : this.selectionModel.select(row);

    const selected = this.#page.filter(benutzer => this.selectionModel.isSelected(benutzer));
    const deselected = this.#page.filter(benutzer => !this.selectionModel.isSelected(benutzer));
    this.benutzerFacade.selectionsubsetChanged(selected, deselected);
  }

  sortData(sort: Sort) {

    if (this.#adjusting) {
      return;
    }

    const pageDefinition = { ...this.#createActualPageDefinition(), sortDirection: sort.direction.toString() };

    if (this.sucheDisabled()) {
      this.benutzerFacade.benutzersucheChanged(this.#createActualFilterAndSort(), pageDefinition);
    } else {
      this.findBenutzer();
    }
  }

  getDisplayedColumns(): string[] {
    return [AUSWAHL_BENUTZER, UUID, EMAIL, NACHNAME, VORNAME, DATE_MODIFIED, ROLLE, TOGGLE_ACTIVATION_STATE_ACTION, DELETE_BENUTZER_ACTION];
  }

  onPaginatorChanged(_event: PageEvent): void {

    if (this.#adjusting) {
      return;
    }

    this.allRowsSelected = false;

    if (!this.sucheDisabled()) {
      this.findBenutzer();
    } else {
      this.benutzerFacade.benutzersucheChanged(this.#createActualFilterAndSort(), this.#createActualPageDefinition());
    }
  }

  toggleSelectAll(event$: any): void {

    if (!this.allRowsSelected) {
      this.selectionModel.clear();
      this.selectionModel.select(...this.#page);
      this.allRowsSelected = true;
      this.benutzerFacade.selectionsubsetChanged(this.#page, []);
    } else {
      this.selectionModel.clear();
      this.allRowsSelected = false;
      this.benutzerFacade.selectionsubsetChanged([], this.#page);
    }
  }

  findBenutzer() {
    const pageDefinition: PageDefinition = this.#createActualPageDefinition();
    const filter: BenutzersucheFilterAndSortValues = this.#createActualFilterAndSort();
    this.benutzerFacade.triggerSearch(filter, pageDefinition);
  }

  sucheDisabled(): boolean {
    const filterAndSort = this.#createActualFilterAndSort();
    return isFilterAndSortEmpty(filterAndSort);
  }

  resetFilter() {
    // reset Paginator when filter is reseted
    this.paginator.pageIndex = 0;
    this.#initFilter(initialBenutzersucheFilterAndSortValues);
    this.benutzerFacade.resetFilterAndSort();
  }

  gotoBasket(): void {
    this.#router.navigateByUrl('users/basket');
  }

  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //    subscriptions in order to sync store and controls
  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  #registerSubscribers(): void {

    // reset the paginator when sort changes
    const matSortChangedSubscription = this.sort.sortChange.subscribe(() => {
      this.paginator.pageIndex = 0;
      this.allRowsSelected = false;
    });
    this.#subscriptions.add(matSortChangedSubscription);

    const pageAndBasketSubscription = combineLatest([this.benutzerFacade.page$, this.benutzerFacade.benutzerBasket$]).subscribe(
      ([page, basket]) => {
        this.#page = page;
        if (page.length > 0) {
          this.#synchronizeSelectioModelWithBenutzerBasket(basket);
        }
      });

    this.#subscriptions.add(pageAndBasketSubscription);
  }

  #synchronizeSelectioModelWithBenutzerBasket(benutzerBasket: Benutzer[]): void {
    const selectedVisibleBenutzer = this.#page.filter(benutzer =>
      benutzerBasket.some(b => b.uuid === benutzer.uuid)
    );

    this.selectionModel.clear();
    this.selectionModel.select(...selectedVisibleBenutzer);
  }

  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //    overall initialization
  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  #applyInitialState(paginationState: PaginationState,
    filterValues: BenutzersucheFilterAndSortValues,
    sortDefinition: SortDefinition,
    page: Benutzer[],
    benutzerBasket: Benutzer[]): void {

    if (this.#adjusting) {
      return;
    }

    this.#adjusting = true;

    this.#paginationState = paginationState;

    this.#initFilter(filterValues);
    this.#initPaginator(paginationState);
    this.#initSort(sortDefinition);
    this.#initSelection(page, benutzerBasket);

    // if (page.length > 0) {
    //   this.#synchronizeSelectioModelWithBenutzerBasket(benutzerBasket);
    // }

    this.changeDetector.detectChanges();
  }

  #initFilter(filter: BenutzersucheFilterAndSortValues): void {

    this.uuidFilterValue = filter.uuid;
    this.emailFilterValue = filter.email;
    this.nachnameFilterValue = filter.nachname;
    this.vornameFilterValue = filter.vorname;
    this.dateModifiedFilterValue = filter.dateModified;
    this.rolleFilterValue = filter.rolle;
  }

  #initPaginator(paginationState: PaginationState): void {

    this.paginator.pageIndex = paginationState.pageDefinition.pageIndex;

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

    this.paginator.pageSize = paginationState.pageDefinition.pageSize;
    this.paginator.pageIndex = paginationState.pageDefinition.pageIndex;
  }

  #initSort(sortDefinition: SortDefinition): void {
    this.sort.active = sortDefinition.active;
    this.sort.direction = sortDefinition.direction;

    // damit der Pfeil neben der Spalte auch den korrekten Status anzeigt, Angular nochmal sagen, dass er sich aktualisieren soll.
    this.sort.sortChange.emit({
      active: sortDefinition.active,
      direction: sortDefinition.direction
    });
  }

  #initSelection(page: Benutzer[], benutzerBasket: Benutzer[]): void {

    if (page.length > 0) {
      const selectedVisibleBenutzer = page.filter(benutzer =>
        benutzerBasket.some(b => b.uuid === benutzer.uuid)
      );

      this.selectionModel.clear();
      this.selectionModel.select(...selectedVisibleBenutzer);
    }
  }

  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //    other private methods
  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  #doDeleteBenuzuer(benutzer: Benutzer): void {
    this.benutzerFacade.deleteSingleBenutzer(benutzer);
  }

  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //    mappings
  // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  #createActualFilterAndSort(): BenutzersucheFilterAndSortValues {

    if (this.sort) {
      const matSortDirection = this.sort.direction;
      console.log('matSortDirection=' + matSortDirection);
      
      let sortByLabel = '';

      if (matSortDirection === "") {
        sortByLabel = '';
      } else {
        sortByLabel = this.sort.active;
      }

      const filter: BenutzersucheFilterAndSortValues = {
        dateModified: this.dateModifiedFilterValue,
        email: this.emailFilterValue,
        nachname: this.nachnameFilterValue,
        rolle: this.rolleFilterValue,
        sortByLabelname: sortByLabel,
        uuid: this.uuidFilterValue,
        vorname: this.vornameFilterValue
      };

      return filter;
    } else {
      return {
        dateModified: this.dateModifiedFilterValue,
        email: this.emailFilterValue,
        nachname: this.nachnameFilterValue,
        rolle: this.rolleFilterValue,
        sortByLabelname: null,
        uuid: this.uuidFilterValue,
        vorname: this.vornameFilterValue
      };
    }
  }

  #createActualPageDefinition(): PageDefinition {

    let sortDirection = '';

    if (this.sort) {
      const matSortDirection = this.sort.direction;
      sortDirection = matSortDirection;
    }

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : this.#paginationState.pageDefinition.pageIndex,
      pageSize: this.paginator ? this.paginator.pageSize : this.#paginationState.pageDefinition.pageSize,
      sortDirection: sortDirection
    }

    return pageDefinition;
  }
}
