import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { Aktivierungsstatus, Benutzer, BenutzerSearchResult, BenutzerSuchparameter, BenutzersucheFilterAndSortValues, DeleteBenutzerResponseDto, UpdateBenutzerResponseDto } from "@bv-admin-app/benutzer/model";
import { PageDefinition } from '@bv-admin-app/shared/model'

export const benutzerActions = createActionGroup({
    source: 'benutzer',
    events: {
        'FIND_BENUTZER': props<{ suchparameter: BenutzerSuchparameter }>(),
        'BENUTZER_FOUND': props<{ treffer: BenutzerSearchResult }>(),
        'BENUTZER_PAGEDEFINITION_CHANGED': props<{ pageDefinition: PageDefinition }>(),
        'BENUTZER_FILTER_CHANGED': props<{ filter: BenutzersucheFilterAndSortValues }>(),
        'SELECTIONSUBSET_CHANGED': props<{ actuallySelected: Benutzer[], actuallyDeselected: Benutzer[] }>(),
        'RESET_BENUTZERBASKET': emptyProps,
        'REMOVE_SINGLE_BENUTZER_FROM_BASKET': props<{ benutzer: Benutzer }>(),
        'RESET_FILTER': emptyProps(),
        'DELETE_SINGLE_BENUTZER': props<{ benutzer: Benutzer }>(),
        'SINGLE_BENUTZER_DELETED': props<{ responsePayload: DeleteBenutzerResponseDto }>(),
        'UPDATE_BENUTZER_ACTIVATION_STATE': props<{ uuid: string, aktivierungsstatus: Aktivierungsstatus }>(),
        'BENUTZER_ACTIVATION_STATE_UPDATED': props<{ result: UpdateBenutzerResponseDto }>()
    }
});

