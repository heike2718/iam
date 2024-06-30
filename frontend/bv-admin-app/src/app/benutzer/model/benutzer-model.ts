import { sortedStringify } from "@bv-admin-app/shared/util";

export interface BenutzersucheFilterValues {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rolle: string;
    readonly aktiviert: boolean | null;
    readonly dateModified: string;
    readonly sortByLabelname: string | null;
};

export const initialBenutzersucheFilterValues: BenutzersucheFilterValues = {
    uuid: '',
    vorname: '',
    nachname: '',
    email: '',
    rolle: '',
    aktiviert: null,
    dateModified: '',
    sortByLabelname: null
};

export interface Benutzer {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rollen: string;
    readonly aktiviert: boolean;
    readonly dateModified: string;
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
    readonly aktiviert: boolean | null;
    readonly dateModified: string;
    readonly sortByLabelname: string | null;
    readonly sortDirection: string | null;
    readonly pageIndex: number;
    readonly pageSize: number;
}

export function isBenutzersucheFilterValuesEqual(obj1: BenutzersucheFilterValues, obj2: BenutzersucheFilterValues): boolean {
    return sortedStringify(obj1) === sortedStringify(obj2);
}
