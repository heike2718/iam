import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../services/session.service';
import { Message, MessageService } from '@authprovider-ws/common-messages';
import { LogService } from '@authprovider-ws/common-logging';


@Injectable({
	providedIn: 'root'
})
export class HttpErrorService {

	constructor(private messageService: MessageService
		, private logger: LogService
		, private sessionService: SessionService) { }


	handleError(error: HttpErrorResponse, context: string) {

		if (error instanceof ErrorEvent) {
			this.logger.error(context + ': ErrorEvent occured - ' + JSON.stringify(error));
			throw (error);
		} else {
			if (error.status === 0) {
				this.messageService.error('Der Server ist nicht erreichbar.');
			} else {
				const msg = this.extractMessageObject(error);
				switch (error.status) {
					case 401:
					case 908:
						this.showServerResponseMessage(msg);
						this.sessionService.clearSession();
						break;
					default: {
						if (msg) {
							this.showServerResponseMessage(msg);
						} else {
							this.messageService.error('Es ist ein unerwarteter Fehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de');
						}
					}
				}
			}
		}
	}


	private extractMessageObject(error: HttpErrorResponse): Message {

		if (error.error) {
			const err = error.error;
			return err.message as Message;
		}

		return null;
	}

	private showServerResponseMessage(msg: Message) {
		this.messageService.showMessage(msg);
	}
}

