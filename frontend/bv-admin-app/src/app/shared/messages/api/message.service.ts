import { Injectable, signal } from "@angular/core";
import { Message } from "./message.model";


@Injectable({ providedIn: 'root' })
export class MessageService {

    #messageSignal = signal<Message | undefined>(undefined);

    public message() {
        return this.#messageSignal;
    }

    public info(text: string) {
        this.#add({ message: text, level: 'INFO' });
        setTimeout(() => {
            this.clear();
        }, 3000); // Clear after 3 seconds
    }

    public warn(text: string) {

        this.#add({ message: text, level: 'WARN' });
    }

    public error(text: string) {

        this.#add({ message: text, level: 'ERROR' });
    }

    public clear(): void {

        this.#messageSignal.set(undefined);
    }

    #add(message: Message) {
        this.#messageSignal.set(message);
    }
}
