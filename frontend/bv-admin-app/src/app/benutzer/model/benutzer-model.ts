export interface BenutzersucheGUIModel {
    readonly anzahlTreffer: number;
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rolle: string;
    readonly aktiviert: boolean | null;
    readonly dateModified: string;
    readonly sortByLabelname: string | null; 
    readonly sortDirection: string | null;
    readonly pageSize: number;
    readonly pageIndex: number;
    readonly selectedBenutzer: Benutzer[];
}

export const initialBenutzersucheGUIModel: BenutzersucheGUIModel = {
    anzahlTreffer: 0,
    uuid: '',
    vorname: '',
    nachname: '',
    email: '',
    rolle: '',
    aktiviert: null,
    dateModified: '',
    sortByLabelname: null,
    sortDirection: null,
    pageSize: 25,
    pageIndex: 0,
    selectedBenutzer: []
}

// export interface BenutzerTableFilterAndSort {
//     uuid: string;
//     vorname: string;
//     nachname: string;
//     email: string;
//     rolle: string;
//     aktiviert: boolean | null;
//     dateModified: string;
//     sortByLabelname: string | null; 
//     sortDirection: string | null;
// }


// export const initialBenutzerTableFilterAndSort: BenutzerTableFilterAndSort = {
//     uuid: '',
//     vorname: '',
//     nachname: '',
//     email: '',
//     rolle: '',
//     aktiviert: null,
//     dateModified: '',
//     sortByLabelname: null,
//     sortDirection: null
// }

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


// export interface PageDefinition {
//     readonly pageSize: number,
//     readonly pageIndex: number
// }

// export const initialPageDefinition: PageDefinition = {
//     pageSize: 20,
//     pageIndex: 0
// };

// export interface PaginationState {
//     anzahlTreffer: number;
//     pageDefinition: PageDefinition
// }

// export const initialPaginationState: PaginationState = {
//     anzahlTreffer: 0,
//     pageDefinition: initialPageDefinition
// }

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
