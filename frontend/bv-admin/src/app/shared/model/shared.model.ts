import { SortDirection } from "@angular/material/sort"

export type Jobstatus = 'WAITING' | 'CANCELLED' | 'IN_PROGRESS' | 'COMPLETED' | 'ERRORS'

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
    pageSize: 200,
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

export interface SingleUuidDto {
    readonly uuid: string;
}

// aus BenutzerTrefferlisteItem.
// Die Attribute aktiviert, darfNichtGeloeschtWerden und bannedForMails können über bv-admin geändert werden.
// alle anderen Attribute sind readonly
export interface Benutzer {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rollen: string;
    readonly aktiviert: boolean;
    readonly aenderungsdatum: string;
    readonly cryptoAlgorithm: string;
    readonly anzahlLogins: number;
    readonly darfNichtGeloeschtWerden: boolean;
    readonly bannedForMails: boolean;
}

export interface Infomail {
    readonly uuid: string;
    readonly betreff: string;
    readonly mailtext: string;
    readonly uuidsMailversandauftraege: string[];
}

export interface MailversandauftragRequestDto {
    readonly idInfomailtext: string;
    readonly benutzerUUIDs: string[];
}

export const initialMailversandauftragRequestDto: MailversandauftragRequestDto = {
    idInfomailtext: '',
    benutzerUUIDs: []
}
