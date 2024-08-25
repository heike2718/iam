import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { DeleteMailversandauftragResponseDto, MailversandauftragDetails, MailversandauftragDetailsResponseDto, MailversandauftragOverview, Mailversandgruppe, MailversandgruppeDetails, MailversandgruppeDetailsResponseDto } from '@bv-admin-app/versandauftraege/model';
import { Benutzer, MailversandauftragRequestDto } from "@bv-admin-app/shared/model";

export const versandauftraegeActions = createActionGroup({
    source: 'versandauftraege',
    events: {
        'LOAD_VERSANDAUFTRAEGE': emptyProps(),
        'VERSANDAUFTRAEGE_LOADED': props<{versandauftraege: MailversandauftragOverview[]}>(),
        'SELECT_VERSANDAUFTRAG': props<{versandauftrag: MailversandauftragDetails}>(),
        'LOAD_VERSANDAUFTRAG_DETAILS': props<{uuid: string}>(),
        'VERSANDAUFTRAG_DETAILS_LOADED': props<{responsePayload: MailversandauftragDetailsResponseDto}>(),
        'UNSELECT_VERSANDAUFTRAG': emptyProps(),
        'SCHEDULE_VERSANDAUFTRAG': props<{requestDto: MailversandauftragRequestDto}>(),
        'VERSANDAUFTRAG_SCHEDULED': props<{versandauftrag: MailversandauftragOverview}>(),
        'CANCEL_VERSANDAUFTRAG': props<{uuid: string}>(),
        'DELETE_VERSANDAUFTRAG': props<{uuid: string}>(),
        'VERSANDAUFTRAG_DELETED': props<{responsePayload: DeleteMailversandauftragResponseDto}>(),
        'LOAD_VERSANDGRUPPE': props<{uuid: string}>(),
        'VERSANDGRUPPE_LOADED': props<{responsePayload: MailversandgruppeDetailsResponseDto}>(),
        'VERSANDGRUPPE_SPEICHERN': props<{mailversandgruppe: MailversandgruppeDetails}>(),
        'VERSANDGRUPPE_BENUTZER_ENTFERNEN': props<{benutzer: Benutzer, mailversandgruppe: MailversandgruppeDetails}>()
    }
})