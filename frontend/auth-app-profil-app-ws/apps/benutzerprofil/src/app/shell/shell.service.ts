import { Injectable, signal } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class ShellService {

    #handset = signal<boolean>(false);

    get isHandset() {
        return this.#handset;
    }

    setHandset(value: boolean) {
        this.#handset.set(value);
    }
}