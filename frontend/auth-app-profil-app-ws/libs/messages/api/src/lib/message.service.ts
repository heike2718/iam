import { Injectable, signal } from "@angular/core";
import { Message } from "@ap-ws/common-model";


@Injectable({
    providedIn: 'root'
})
export class MessageService {

    #messageSignal = signal<Message | undefined>(undefined);

    get message() {
        return this.#messageSignal;
    }

    info(message: string) {
        this.#add({ message: message, level: 'INFO' });
        setTimeout(() => {
            this.clearMessage();
        }, 3000); // nach 3 Sekunden clearen
    }

    warn(message: string) {
        this.#add({ message: message, level: 'WARN' });
    }

    error(message: string) {
        this.#add({ message: message, level: 'ERROR' });
    }

    clearMessage() {
        this.#messageSignal.set(undefined);
    }

    #add(message: Message) {
        this.#messageSignal.set(message);
    }
}