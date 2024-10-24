import { HttpContextToken } from '@angular/common/http';

/** wird gesetzt, wenn die Anwndung Dinge im Hintergrund nachladen will. Damit kann ein sichtbarer loading indicator unterdrückt werden  */
export const SILENT_LOAD_CONTEXT = new HttpContextToken(() => false);

/** https://angular.io/api/common/http/HttpContext */
