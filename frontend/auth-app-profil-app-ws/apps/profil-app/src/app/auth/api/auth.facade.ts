import { inject, Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import { authActions, fromAuth } from "@profil-app/auth/data";
import { Observable, of, switchMap } from "rxjs";
import { anonymousSession, AuthResult, Session } from "@profil-app/auth/model";
import { BenutzerdatenFacade } from "@profil-app/benutzerdaten/api";
import { MessageService } from "@ap-ws/messages/api";

@Injectable({
	providedIn: 'root'
})
export class AuthFacade {

	#store = inject(Store);
	#benutzerdatenFacade = inject(BenutzerdatenFacade);
	#currentSession: Session = anonymousSession;
	#messageService = inject(MessageService);

	readonly userIsLoggedIn$: Observable<boolean> = this.#store.select(fromAuth.session).pipe(switchMap((session) => of(session.sessionId !== undefined))
	);

	readonly userIsLoggedOut$: Observable<boolean> = this.userIsLoggedIn$.pipe(
		switchMap((li) => of(!li))
	);

	constructor() {
		this.#store.select(fromAuth.session).subscribe((session) => this.#currentSession = {...session});

		this.#messageService.securityEvent$.subscribe((secEvent) => {
			if (secEvent) {
				this.logout();
			}
		});
	}

	public login(): void {

		this.#store.dispatch(authActions.rEQUEST_LOGIN_URL());
	}

	public logout(): void {

		this.#store.dispatch(authActions.lOG_OUT());
	}

	public initClearOrRestoreSession(): void {

		const hash = window.location.hash;

		if (hash && hash.indexOf('idToken') > 0) {

			this.#initSession(hash);
		} else {

			this.#reloadSession();
		}
	}

	#parseHash(hash: string): AuthResult {

		hash = hash.replace(/^#?\/?/, '');

		const result: AuthResult = {
			expiresAt: 0,
			nonce: undefined,
			state: undefined,
			idToken: undefined
		};

		if (hash.length > 0) {

			const tokens = hash.split('&');
			tokens.forEach(
				(token) => {
					const keyVal = token.split('=');
					switch (keyVal[0]) {
						case 'expiresAt': result.expiresAt = JSON.parse(keyVal[1]); break;
						case 'nonce': result.nonce = keyVal[1]; break;
						case 'state': result.state = keyVal[1]; break;
						case 'idToken': result.idToken = keyVal[1]; break;
					}
				}
			);
		}
		window.location.hash = '';
		return result;
	}

	#initSession(hash: string) {
		const authResult: AuthResult = this.#parseHash(hash);

		if (authResult.state) {
			if (authResult.state === 'login') {
				this.#store.dispatch(authActions.cREATE_SESSION({ authResult }));
			}
		} else {
			window.location.hash = '';
		}
	}

	#reloadSession() {
		if (this.#currentSession.sessionId) {
			if (Date.now() > this.#currentSession.expiresAt) {
				this.logout();
			} else {
				this.#benutzerdatenFacade.benutzerdatenLaden();
			}
		}
	}
}