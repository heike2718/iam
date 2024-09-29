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
        console.log('loading indicator started');
        setTimeout(() => {
            this.stop();
        }, 3000);
    }

    stop(): void {
        this.#loadingSignal.set(false);
        console.log('loading indicator stopped');
    }
}
