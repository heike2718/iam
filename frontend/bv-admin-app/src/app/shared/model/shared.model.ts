export interface PageDefinition {
    pageSize: number,
    pageIndex: number,
    sortDirection: string
};

export interface PaginationState {
    anzahlTreffer: number;
    pageDefinition: PageDefinition
};

export const initialPageDefinition: PageDefinition = {
    pageSize: 20,
    pageIndex: 0,
    sortDirection: 'asc'
};

export const initialPaginationState: PaginationState = {
    anzahlTreffer: 0,
    pageDefinition: initialPageDefinition
};

