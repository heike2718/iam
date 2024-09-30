import { Injectable, signal } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class LoadingService {

    #loadingSignal = signal<boolean>(false);

    get loading() {
        return this.#loadingSignal;
    }

    start(): void {
        this.#loadingSignal.set(true);
    }

    stop(): void {
        this.#loadingSignal.set(false);
    }
}
