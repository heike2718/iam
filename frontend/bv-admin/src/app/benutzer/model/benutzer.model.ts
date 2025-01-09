import { Benutzer } from "@bv-admin/shared/model";
import { sortedStringify } from "@bv-admin/shared/util";

export interface BenutzersucheFilterValues {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rolle: string;
    readonly aenderungsdatum: string;
}

export const initialBenutzersucheFilterValues: BenutzersucheFilterValues = {
    uuid: '',
    vorname: '',
    nachname: '',
    email: '',
    rolle: '',
    aenderungsdatum: ''
}

export interface BenutzersucheFilterAndSortValues {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rolle: string;
    readonly aenderungsdatum: string;
    readonly sortByLabelname: string | null;
}

export const initialBenutzersucheFilterAndSortValues: BenutzersucheFilterAndSortValues = {
    uuid: '',
    vorname: '',
    nachname: '',
    email: '',
    rolle: '',
    aenderungsdatum: '',
    sortByLabelname: ''
}

export interface UpdateBenutzerResponseDto {
    readonly uuid: string;
    readonly benuzer?: Benutzer; 
}

export interface BenutzerSearchResult {
    readonly anzahlGesamt: number;
    readonly items: Benutzer[];
}

export interface BenutzerSuchparameter {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rolle: string;
    readonly aenderungsdatum: string;
    readonly sortByLabelname: string | null;
    readonly sortDirection: string | null;
    readonly pageIndex: number;
    readonly pageSize: number;
}

export interface Aktivierungsstatus{
    readonly aktiviert: boolean;
}

export interface DeleteBenutzerResponseDto {
    readonly uuid: string;
}

export function isFilterEmpty(filter: BenutzersucheFilterValues): boolean {
    const str1 = sortedStringify(initialBenutzersucheFilterValues);
    const str2 = sortedStringify(filter);
    return str1 === str2;
}

export function isFilterAndSortEmpty(filterAndSort: BenutzersucheFilterAndSortValues): boolean {

    const filter: BenutzersucheFilterValues = {
        aenderungsdatum: filterAndSort.aenderungsdatum,
        email: filterAndSort.email,
        nachname: filterAndSort.nachname,
        rolle: filterAndSort.rolle,
        uuid: filterAndSort.uuid,
        vorname: filterAndSort.vorname
    };

    if (!isFilterEmpty(filter)) {
        return false;
    }

    return initialBenutzersucheFilterAndSortValues.sortByLabelname === filterAndSort.sortByLabelname;
}

export function removeBenutzers(benutzer: Benutzer[], benutzerToRemove: Benutzer[]): Benutzer[] {
    const benutzerToRemoveIds = new Set(benutzerToRemove.map(b => b.uuid));
    return benutzer.filter(b => !benutzerToRemoveIds.has(b.uuid));
}

export function addBenutzerIfNotContained(benutzer: Benutzer[], benutzerToAdd: Benutzer[]): Benutzer[] {
    const existingIds = new Set(benutzer.map(b => b.uuid));
    const uniqueNewArray = benutzerToAdd.filter(b => !existingIds.has(b.uuid));
    return benutzer.concat(uniqueNewArray);
}

export function removeBenutzer(benutzer: Benutzer[], uuid: string): Benutzer[] {
    return benutzer.filter(b => b.uuid !== uuid);
}


  
