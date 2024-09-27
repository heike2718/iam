import { Benutzer, Jobstatus } from "@bv-admin-app/shared/model";
import { parseGermanDate, parseGermanDateTime } from "@bv-admin-app/shared/util";

export interface Mailversandgruppe {
    readonly uuid: string;
    readonly idMailversandauftrag: string;
    readonly sortnr: number;
    readonly status: Jobstatus;
    readonly aenderungsdatum: string;
    readonly empfaengerUUIDs: string[];
}

export interface MailversandgruppeDetails {
    readonly uuid: string;
    readonly idInfomailtext: string;
    readonly sortnr: number;
    readonly status: Jobstatus;
    readonly aenderungsdatum: string;
    readonly benutzer: Benutzer[];
}

export interface MailversandgruppeDetailsResponseDto {
    readonly uuid: string;
    readonly mailversandgruppe: MailversandgruppeDetails;
}

export interface MailversandauftragOverview {
    readonly uuid: string;
    readonly idInfomailtext: string;
    readonly betreff: string;
    readonly erfasstAm: string;
    readonly status: Jobstatus;
    readonly anzahlEmpfaenger: number;
    readonly anzahlGruppen: number;
}

export interface MailversandauftragDetails {
    readonly uuid: string;
    readonly idInfomailtext: string;
    readonly betreff: string;
    readonly mailtext: string;
    readonly versandJahrMonat: string;
    readonly status: Jobstatus;
    readonly erfasstAm: string;
    readonly versandBegonnenAm: string;
    readonly versandBeendetAm: string;
    readonly anzahlEmpfaenger: number;
    readonly anzahlVersendet: number;
    readonly versandMitFehlern: boolean;
    readonly mailversandgruppen: Mailversandgruppe[];
}

export interface MailversandauftragStatistik {
    readonly completed: number;
    readonly errors: number;
    readonly waiting: number;
    readonly inProgress: number;
    readonly cancelled: number;
}

export interface MailversandauftragUIModel {
    readonly mailversandauftrag: MailversandauftragDetails;
    readonly statistik: MailversandauftragStatistik;
}

export const initialMailversandauftraStatistik: MailversandauftragStatistik = {
    cancelled: 0,
    completed: 0,
    errors: 0,
    inProgress: 0,
    waiting: 0
}

export interface DeleteMailversandauftragResponseDto {
    readonly uuid: string
}

export interface MailversandauftragDetailsResponseDto {
    readonly uuid: string;
    readonly versandauftrag: MailversandauftragDetails | undefined;
}


export function sortMailversandauftragOverviewByDate(versandauftraege: MailversandauftragOverview[]): MailversandauftragOverview[] {

    return versandauftraege.sort((a: MailversandauftragOverview, b: MailversandauftragOverview) => {

        const dateA = parseGermanDateTime(a.erfasstAm);
        const dateB = parseGermanDateTime(b.erfasstAm);

        return dateB.getTime() - dateA.getTime();
    });
}

export function computeStatistics(versandauftrag: MailversandauftragDetails): MailversandauftragUIModel {


    const cancelled = versandauftrag.mailversandgruppen.filter(g => g.status === 'CANCELLED').length;
    const completed = versandauftrag.mailversandgruppen.filter(g => g.status === 'COMPLETED').length;
    const errors = versandauftrag.mailversandgruppen.filter(g => g.status === 'ERRORS').length;
    const inProgress = versandauftrag.mailversandgruppen.filter(g => g.status === 'IN_PROGRESS').length;
    const waiting = versandauftrag.mailversandgruppen.filter(g => g.status === 'WAITING').length;

    const statistik: MailversandauftragStatistik = {
        cancelled: cancelled,
        completed: completed,
        errors: errors,
        inProgress: inProgress,
        waiting: waiting
    };

    return {
        mailversandauftrag: { ...versandauftrag },
        statistik: statistik
    };
}

export function mapToOverview(mailversandgruppe: MailversandgruppeDetails, idMailversandauftrag: string): Mailversandgruppe {

    const empfaengerUUIDs: string[] =  mailversandgruppe.benutzer.map(b => b.uuid);

    const result: Mailversandgruppe = {
        aenderungsdatum: mailversandgruppe.aenderungsdatum,
        empfaengerUUIDs: empfaengerUUIDs,
        idMailversandauftrag: idMailversandauftrag,
        sortnr: mailversandgruppe.sortnr,
        status: mailversandgruppe.status,
        uuid: mailversandgruppe.uuid
    };

    return result;
}
