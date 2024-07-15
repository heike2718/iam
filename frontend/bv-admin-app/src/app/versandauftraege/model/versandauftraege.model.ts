import { Jobstatus } from "@bv-admin-app/shared/model";

export interface MailversandauftragRequestDto {
    readonly idInfomailtext: string;
    readonly benutzerIds: number[];
}

export const initialMailversandauftragRequestDto: MailversandauftragRequestDto = {
    idInfomailtext: '',
    benutzerIds: []
}

export interface Mailversandgruppe {
    readonly uuid: string;
    readonly sortnr: number;
    readonly status: Jobstatus;
    readonly empfaengerEmails: string[];
}

export interface MailversandauftragOverview {
    readonly uuid: string;
    readonly idInfomailtext: string;
    readonly betreff: string;
    readonly status: Jobstatus;
    readonly anzahlEmpfaenger: number;
    readonly anzahlGruppen: number;
}

export interface MailversandauftragDetails {
    readonly uuid: string;
    readonly idInfomailtext: string;
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

export interface DeleteMailversandauftragResponseDto {
    readonly uuid: string
}


export function sortMailversandauftragOverviewByBetreff(versandauftraege: MailversandauftragOverview[]): MailversandauftragOverview[] {
    return versandauftraege.slice().sort((a, b) => a.betreff.localeCompare(b.betreff));
}

