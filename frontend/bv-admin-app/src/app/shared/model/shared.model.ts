import { SortDirection } from "@angular/material/sort"

export type Jobstatus = 'WAITING' | 'IN_PROGRESS' | 'COMPLETED' | 'ERRORS'

export interface PageDefinition {
    pageSize: number,
    pageIndex: number,
    sortDirection: string
}

export interface PaginationState {
    anzahlTreffer: number
    pageDefinition: PageDefinition
}

export const initialPageDefinition: PageDefinition = {
    pageSize: 25,
    pageIndex: 0,
    sortDirection: ''
}

export const initialPaginationState: PaginationState = {
    anzahlTreffer: 0,
    pageDefinition: initialPageDefinition
}

export interface SortDefinition {
    readonly direction: SortDirection
    readonly active: string
}

export interface Benutzer {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rollen: string;
    readonly aktiviert: boolean;
    readonly aenderungsdatum: string;
}