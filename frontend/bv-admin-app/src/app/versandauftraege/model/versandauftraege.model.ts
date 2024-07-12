import { Jobstatus } from "@bv-admin-app/shared/model";

export interface MailversandauftragRequestDto {
    readonly idInfomailtext: string;
    readonly benutzerIds: number[];
}

export const initialMailversandauftragRequestDto: MailversandauftragRequestDto = {
    idInfomailtext: '',
    benutzerIds: []
}

export interface MailversandauftragOverview {
    readonly uuid: string;
    readonly betreff: string;
    readonly status: Jobstatus;
    readonly versandBegonnenAm: string;
    readonly versandBeendetAm: string;
    readonly anzahlEmpfaenger: number;
    readonly anzahlGruppen: number;
    readonly anzahlVersendet: number;
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
    readonly infomailgruppen: Infomailgruppe[];
}
