import { ZweiPassworte } from "@ap-ws/common-model";
import { ClientCredentials, TwoPasswords } from "@authprovider/model";

export interface SignUpFormModel {
    email: string;
    loginName?: string;
    vorname?: string;
    nachname?: string;
    agbGelesen: boolean;
    twoPasswords: TwoPasswords;
    kleber?: string;
}

export const initialSignUpFormModel: SignUpFormModel = {
    email: '',
    loginName: undefined,
    vorname: undefined,
    nachname: undefined,
    agbGelesen: false,
    twoPasswords: {
        passwort: '',
        passwortWdh: ''
    },
    kleber: '',
}

export interface SignUpCredentials {
    email: string;
    loginName?: string;
    vorname?: string;
    nachname?: string;
    nonce?: string;
    agbGelesen: boolean;
    zweiPassworte: ZweiPassworte;
    kleber?: string;
    clientCredentials: ClientCredentials;
}

