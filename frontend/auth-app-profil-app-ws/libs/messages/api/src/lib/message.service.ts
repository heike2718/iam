import { Injectable } from "@angular/core";
import { Message } from "@ap-ws/common-model";
import { BehaviorSubject, Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class MessageService {

    #messageSubject = new BehaviorSubject<Message | undefined>(undefined);
    #securityEvent = new BehaviorSubject<boolean>(false);
        
    message$: Observable<Message | undefined> = this.#messageSubject.asObservable();
    securityEvent$: Observable<boolean> = this.#securityEvent.asObservable();
    
    #currentMessage: Message | undefined;
    #adjusting = false;

    constructor() {
        this.message$.subscribe((message) => {
            if (!this.#adjusting) {
                this.#currentMessage = message;
            }
        });
    }

    info(text: string, isSecurityEvent: boolean, timeoutMs: number) {

        const message: Message = { message: text, level: 'INFO', securityEvent: isSecurityEvent };

        this.#add(message);
        setTimeout(() => {
            if (message.securityEvent) {
                this.#adjusting = true;
            }
            this.clearMessage();
        }, timeoutMs);
    }

    warn(text: string) {
        this.#add({ message: text, level: 'WARN', securityEvent: false });
    }

    error(message: string) {
        this.#add({ message: message, level: 'ERROR', securityEvent: false });
    }

    clearMessage() {

        if (this.#currentMessage && this.#currentMessage.securityEvent) {
            this.#securityEvent.next(this.#currentMessage.securityEvent);
        }
        this.#adjusting = false;
        this.#messageSubject.next(undefined);
    }

    clearSecurityEvent(): void {
        this.#securityEvent.next(false);
    }

    #add(message: Message) {
       this.#messageSubject.next(message);
    }
}