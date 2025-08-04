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

export interface ChangeBenutzerdatenResponseDto {
    readonly benutzer: Benutzerdaten;
    readonly securityEvent: boolean;
}

export function isAnonymerBenutzer(benutzerdaten: Benutzerdaten): boolean {

    return benutzerdaten.nachname.length === 0 &&
        benutzerdaten.vorname.length === 0;
}

export function fullName(benutzerdaten: Benutzerdaten): string {
    return benutzerdaten.vorname + ' ' + benutzerdaten.nachname;

}

/**
 * Compares two Benutzerdaten objects and returns true if all attributes are equal, false otherwise.
 * @param benutzer1 The first Benutzerdaten object.
 * @param benutzer2 The second Benutzerdaten object.
 * @returns boolean indicating if both Benutzerdaten objects are equal.
 */
export function benutzerAreEqual(benutzer1: Benutzerdaten, benutzer2: Benutzerdaten): boolean {

    return (

        benutzer1.loginName.trim() === benutzer2.loginName.trim() &&
        benutzer1.email.trim() === benutzer2.email.trim() &&
        benutzer1.vorname.trim() === benutzer2.vorname.trim() &&
        benutzer1.nachname.trim() === benutzer2.nachname.trim()
    );
}