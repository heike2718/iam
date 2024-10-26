import { Injectable, signal } from "@angular/core";
import { Message, noopMessage } from "./message.model";
import { BehaviorSubject, Observable } from "rxjs";


@Injectable({ providedIn: 'root' })
export class MessageService {

    // #messageSignal = signal<Message>(noopMessage);

    #messageSubject = new BehaviorSubject(noopMessage);

    message$: Observable<Message> = this.#messageSubject.asObservable();

    // public message() {
    //     return this.#messageSignal;
    // }

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

        // this.#messageSignal.set(noopMessage);
        this.#messageSubject.next(noopMessage);
    }

    #add(message: Message) {
        // this.#messageSignal.set(message);
        this.#messageSubject.next(message);
    }
}
