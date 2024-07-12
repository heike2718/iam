import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { MailversandauftragDetails, MailversandauftragOverview } from '@bv-admin-app/versandauftraege/model';

export const versandauftraegeActions = createActionGroup({
    source: 'versandauftraege',
    events: {
        'LOAD_VERSANDAUFTRAEGE': emptyProps(),
        'VERSANDAUFTRAEGE_LOADED': props<{versandauftraege: MailversandauftragOverview[]}>(),
        'SELECT_VERSANDAUFTRAG': props<{versandauftrag: MailversandauftragOverview}>(),
        'LOAD_VERSANDAUFTRA_DETAILS': props<{uuid: string}>(),
        'VERSANDAUFTRAG_DETAILS_LOADED': props<{versandauftrag: MailversandauftragDetails}>(),
        'UNSELECT_VERSANDAUFTRAG': emptyProps()
    }
})