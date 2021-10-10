import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ClientCredentials } from '../shared/model/auth-model';
import { MessageService, Message } from '@authprovider-ws/common-messages';
import { LogService } from '@authprovider-ws/common-logging';

@Injectable({
	providedIn: 'root'
})
export class HttpErrorService {

	constructor(private messageService: MessageService, private logger: LogService) { }

	handleError(error: HttpErrorResponse, context: string, clientCredentials: ClientCredentials) {

		if (error instanceof ErrorEvent) {
			this.logger.error(context + ': ErrorEvent occured - ' + JSON.stringify(error));
			throw (error);
		} else {
			switch (error.status) {
				case 0:
					this.messageService.error('Der Server ist nicht erreichbar.');
					break;
				case 904:
					if (context === 'getClient' && clientCredentials) {
						if (clientCredentials.redirectUrl) {
							window.location.href = clientCredentials.redirectUrl;
						}
					} else {
						this.handleTheError(error, context);
					}
					break;
				default:
					this.handleTheError(error, context);
			}
		}
	}

	private handleTheError(error: HttpErrorResponse, context: string): void {
		this.logger.error(context + ': Servererror status=' + error.status);
		const msg = this.extractMessageObject(error);
		if (msg !== null) {
			this.showServerResponseMessage(msg);
		} else {
			this.messageService.error('Es ist ein unerwarteter Fehler aufgetreten. Bitte senden Sie eine Mail an mathe@egladil.de');
		}
	}

	private extractMessageObject(error: HttpErrorResponse): Message {

		if (error['_body']) {
			// so bekommt man den body als nettes kleines JSON-Objekt :)
			const body = JSON.parse(error['_body']);
			if (body['message']) {
				return <Message>body['message'];
			}
		}

		if (error['error']) {
			const err = error['error'];
			return <Message>err['message'];
		}

		return null;
	}

	private showServerResponseMessage(msg: Message) {
		switch (msg.level) {
			case 'WARN':
				this.messageService.error(msg.message);
				break;
			case 'ERROR':
				this.messageService.error(msg.message);
				break;
			default:
				this.messageService.error('Unbekanntes message.level ' + msg.level + ' vom Server bekommen.');
		}
	}
}
