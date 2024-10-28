// tslint:disable-next-line:max-line-length

/** Konstanten für Pattern-Validierung im Frontend. */


export const REG_EXP_PASSWORD = /(?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !#\$%&amp;'\(\)\*\+,\-\.\/:;=\?@\[\]\^\\ _`'{|}~]{8,100}/;

export const REG_EXP_PASSWORT_NEU = /^(?=.*\d)(?=.*[a-zA-ZäÄöÖüÜß])(?=.*[!#$%&()*+,\-./:;=?@\[\]^_`'{|}~])[\da-zA-ZäÄöÖüÜß!#$%&()*+,\-./:;=?@\[\]^_`'{|}~]{12,100}$/;

export const REG_EXP_EMAIL = /^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

export const sonderzeichen = '!#$%&)(*+,-./:;=?@][^ _`\'{|}~';

export const buchstaben = 'a-z, A-Z, ÄÖÜäöüß';

export const PASSWORTREGELN = ' Buchstaben, Ziffern und Sonderzeichen, keine Leerzeichen, mindestens 12 und höchstens 100 Zeichen. '
+ 'Erlaubte Sonderzeichen sind ' + sonderzeichen + ', erlaubte Buchstaben sind ' + buchstaben + '.';
 