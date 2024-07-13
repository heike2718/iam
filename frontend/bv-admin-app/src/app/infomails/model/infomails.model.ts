import { Jobstatus } from "@bv-admin-app/shared/model";

export interface InfomailRequestDto {
    readonly betreff: string;
    readonly mailtext: string;
}

export const initialInfomailRequestDto: InfomailRequestDto = {
    betreff: '',
    mailtext: ''
}

export interface Infomail {
    readonly uuid: string;
    readonly betreff: string;
    readonly mailtext: string;
    readonly uuidsMailversandauftraege: string[];
}

export interface Infomailgruppe {
    readonly uuid: string;
    readonly sortnr: number;
    readonly status: Jobstatus;
    readonly empfaengerEmails: string[];
}


export interface UpdateInfomailResponseDto {
    readonly uuid: string,
    readonly infomail?: Infomail;
}


export function sortInfomailsByBetreff(infomails: Infomail[]): Infomail[] {
    return infomails.slice().sort((a, b) => a.betreff.localeCompare(b.betreff));
}


