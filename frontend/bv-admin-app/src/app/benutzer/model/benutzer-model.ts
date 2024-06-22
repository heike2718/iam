export interface BenutzerTableFilter {
    readonly uuid: string | null;
    readonly vorname: string | null;
    readonly nachname: string | null;
    readonly email: string | null;
    readonly rolle: string | null;
    readonly aktiviert: boolean | null;
    readonly dateModified: string | null;
}


export const initialBenutzerTableFilter: BenutzerTableFilter = {
    uuid: null,
    vorname: null,
    nachname: null,
    email: null,
    rolle: null,
    aktiviert: null,
    dateModified: null
}

export interface Benutzer {
    readonly uuid: string;
    readonly vorname: string;
    readonly nachname: string;
    readonly email: string;
    readonly rolle: string;
    readonly aktiviert: boolean;
    readonly dateModified: string;
}

export interface BenutzerSearchResult {
    readonly anzahlGesamt: number;
    readonly items: Benutzer[];
}


export interface PageDefinition {
    readonly sortByLabelname: string;
    readonly pageSize: number,
    readonly pageIndex: number,
    readonly sortDirection: string
}

export const initialPageDefinition: PageDefinition = {
    sortByLabelname: 'nachname',
    pageSize: 20,
    pageIndex: 0,
    sortDirection: 'asc'
};

export interface PaginationState {
    anzahlTreffer: number;
    pageDefinition: PageDefinition
}

export const initialPaginationState: PaginationState = {
    anzahlTreffer: 0,
    pageDefinition: initialPageDefinition
}

export interface BenutzerSuchparameter {
    readonly uuid: string | null;
    readonly vorname: string | null;
    readonly nachname: string | null;
    readonly email: string | null;
    readonly rolle: string | null;
    readonly aktiviert: boolean | null;
    readonly dateModified: string | null;
    readonly sortByLabelname: string;
    readonly sortDirection: string;
    readonly pageIndex: number;
    readonly pageSize: number;
}
