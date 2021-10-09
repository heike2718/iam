import { NgbModalOptions } from "@ng-bootstrap/ng-bootstrap";

export const modalOptions: NgbModalOptions = {
    backdrop:'static',
    centered:true,
    ariaLabelledBy: 'modal-basic-title'
}

export const sonderzeichen = '!"#$%&)(*+,-./:;<=>?@][^ _`\'{|}~';

export const PASSWORTREGELN = 'mindestens 8 Zeichen, höchstens 100 Zeichen, mindestens ein Buchstabe, mindestens eine Ziffer, keine Leerzeichen am Anfang und am Ende, '
+ 'erlaubte Sonderzeichen sind ' + sonderzeichen;

export const MAILADRESSE_REGISTRIERT = 'Tragen Sie hier bitte die Mailadresse ein, mit der Sie sich registriert haben.';

export const MAILADRESSE_REGISTRIERUNG = 'Mit dieser Mailadresse werden Sie sich später einloggen.';

export const TEMPPWD = 'Tragen Sie hier bitte das Einmalpasswort ein, das Ihnen per Mail gesendet wurde.';

export function calculateSpyTime(pwd: string): number {
    
    if (pwd.length <= 8) {
      return 2000;
    }

    return pwd.length * 250 + 1;
  }
