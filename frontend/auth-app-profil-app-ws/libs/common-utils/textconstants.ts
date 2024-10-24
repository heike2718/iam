// tslint:disable-next-line:max-line-length

export const REG_EXP_PASSWORD = /(?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !#\$%&amp;'\(\)\*\+,\-\.\/:;=\?@\[\]\^\\ _`'{|}~]{8,100}/;
export const REG_EXP_EMAIL = /^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

export const sonderzeichen = '!#$%&)(*+,-./:;=?@][^ _`\'{|}~';

export const PASSWORTREGELN = 'mindestens 8 Zeichen, höchstens 100 Zeichen, mindestens ein Buchstabe, mindestens eine Ziffer, keine Leerzeichen am Anfang und am Ende, '
+ 'erlaubte Sonderzeichen sind ' + sonderzeichen;
