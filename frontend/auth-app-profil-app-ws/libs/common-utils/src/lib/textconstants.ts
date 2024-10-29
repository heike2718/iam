// tslint:disable-next-line:max-line-length

/** Konstanten für Pattern-Validierung im Frontend. */


export const REG_EXP_PASSWORD = /^[\da-zA-ZäÄöÖüÜß!#$%&()*+,\-./:;=?@\[\]^_`'{|}~]*$/;

export const REG_EXP_PASSWORT_NEU = /^(?!\s)(?=.*\d)(?=.*[a-zA-ZäÄöÖüÜß])(?=.*[!#$%&()*+,\-./:;=?@\[\]^_`'{|}~])[a-zA-ZäÄöÖüÜß\d!#$%&()*+,\-./:;=?@\[\]^_`'{|}~ ]*(?<!\s)$/;

export const REG_EXP_EMAIL = /^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

export const sonderzeichen = '!#$%&)(*+,-./:;=?@][^ _`\'{|}~';

export const buchstaben = 'a-z, A-Z, ÄÖÜäöüß';

export const PASSWORT_ERLAUBTE_ZEICHEN = buchstaben + ', 0-9, und Sonderzeichen außer > " <. Leerzeichen am Anfang und am Ende sind nicht erlaubt.';

export const PASSWORTREGELN = 'Komplexe Passwörter enthalten Buchstaben, Ziffern und Sonderzeichen. Ihr Passwort muss daher mindestens einen Buchstaben, eine Ziffer und ein Sonderzeichen enthalten.'
    + ' Die Sonderzeichen > " < sind nicht erlaubt. Erlaubte Buchstaben sind ' + buchstaben + '. Leerzeichen am Anfang und am Ende sind nicht erlaubt.'; 