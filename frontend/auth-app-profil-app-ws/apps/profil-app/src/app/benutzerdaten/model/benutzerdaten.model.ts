export interface Benutzerdaten {
    readonly loginName: string;
    readonly email: string;
    readonly vorname: string;
    readonly nachname: string;
}

export const anonymeBenutzerdaten: Benutzerdaten = {
    loginName: '',
    email: '',
    nachname: 'Anonym',
    vorname: 'Gast'
}

export function isAnonymerBenutzer(benutzerdaten: Benutzerdaten): boolean {

    return benutzerdaten.nachname === 'Anonym' && benutzerdaten.vorname === 'Gast';
}

export function fullName(benutzerdaten: Benutzerdaten): string {

    return benutzerdaten.vorname + ' ' + benutzerdaten.nachname;

}