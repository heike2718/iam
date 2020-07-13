import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MessagesService, Message, WARN, ERROR, LogService } from 'hewi-ng-lib';
import { SessionService } from '../services/session.service';


@Injectable({
	providedIn: 'root'
})
export class HttpErrorService {

	constructor(private messagesService: MessagesService
		, private logger: LogService
		, private sessionService: SessionService) { }


	handleError(error: HttpErrorResponse, context: string) {

		if (error instanceof ErrorEvent) {
			this.logger.error(context + ': ErrorEvent occured - ' + JSON.stringify(error));
			throw (error);
		} else {
			if (error.status === 0) {
				this.messagesService.error('Der Server ist nicht erreichbar.');
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
							this.messagesService.error('Es ist ein unerwarteter Fehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de');
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
		switch (msg.level) {
			case WARN:
				this.messagesService.error(msg.message);
				break;
			case ERROR:
				this.messagesService.error(msg.message);
				break;
			default:
				this.messagesService.error('Unbekanntes message.level ' + msg.level + ' vom Server bekommen.');
		}
	}
}

