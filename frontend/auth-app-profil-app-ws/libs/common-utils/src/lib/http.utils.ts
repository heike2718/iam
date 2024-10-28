import { HttpContext, HttpContextToken, HttpErrorResponse } from "@angular/common/http";
import { Message } from "@ap-ws/common-model";


const defaultErrorMessage = 'Ups, da ist leider ein Fehler aufgetreten.';

export const OPEN_API_HTTP_TOKEN = new HttpContextToken<boolean>(() => false);

/** kann als key in einem key-value pair mittels withErrorMessageContext() an einen HttpRequest mitgegeben werden, 
 * um eine kontextbezogene Fehlermeldung zu erzeugen.
 */
export const ERROR_MESSAGE_CONTEXT = new HttpContextToken(() => defaultErrorMessage);

export interface ConstraintViolation {
    readonly fieldName: string;
    readonly message: string;
}


export function withErrorMessageContext(message: string) {
    return new HttpContext().set(ERROR_MESSAGE_CONTEXT, message);
}

export function getHttpErrorResponse(error: NonNullable<unknown>): HttpErrorResponse | undefined {

    if (error instanceof HttpErrorResponse) {
        return <HttpErrorResponse>error;
    }

    return undefined;

}

export function extractServerErrorMessage(error: HttpErrorResponse): Message {

    if (error.status === 0) {
        return { level: 'ERROR', message: 'Der Server ist nicht erreichbar.' };
    }

    const errorResponse: HttpErrorResponse = <HttpErrorResponse>error;

    if (window.location.hash.indexOf('idToken') >= 0) {
        window.location.hash = '';
    }

    if (errorResponse.status === 400) {
        const error = errorResponse.error;

        if (Array.isArray(error)) {

            const violations: ConstraintViolation[] = error as ConstraintViolation[];
            const message = violations.map(cv => `${cv.fieldName}: ${cv.message}`).join(', ');            

            return { level: 'ERROR', message: 'Upsi, da ist im frontend anscheinend etwas schiefgelaufen: ' + message };
        } else {
            const payload: Message = errorResponse.error;

            if (payload) {
                return payload;
            }
        }
    } else {
        const payload: Message = errorResponse.error;

        if (payload && payload.level && payload.message) {
            return { message: payload.message, level: payload.level };
        }
    }

    return getGenericMessageForStatus(error.status);
}


function getGenericMessageForStatus(status: number): Message {

    let result = '';

    switch (status) {
        case 400: result = 'Payload oder Queryparameter haben Fehler'; break;
        case 401: result = 'keine Berechtigung (401)'; break;
        case 403: result = 'unerlaubte Aktion (403)'; break;
        case 404: result = 'Das Objekt existiert nicht (404)'; break;
        case 409: result = 'Die Aktion ist wegen eines Datenkonflikts nicht möglich. Bitte kontaktieren Sie die im Impressum angegebene Mailadresse'; break;
        default: result = 'Im Backend ist ein unerwarteter Fehler aufgetreten.';
    }

    return { level: 'ERROR', message: result };
}