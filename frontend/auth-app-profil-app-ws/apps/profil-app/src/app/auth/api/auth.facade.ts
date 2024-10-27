import { inject, Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import { authActions, fromAuth } from "@profil-app/auth/data";
import { Observable, of, switchMap } from "rxjs";
import { AuthResult } from "@profil-app/auth/model";

@Injectable({
	providedIn: 'root'
})
export class AuthFacade {

	#store = inject(Store);

	readonly userIsLoggedIn$: Observable<boolean> = this.#store.select(fromAuth.session).pipe(switchMap((session) => of(session.sessionId !== undefined))
	);

	readonly userIsLoggedOut$: Observable<boolean> = this.userIsLoggedIn$.pipe(
		switchMap((li) => of(!li))
	);

	public login(): void {

		this.#store.dispatch(authActions.rEQUEST_LOGIN_URL());
	}

	public logout(): void {

		this.#store.dispatch(authActions.lOG_OUT());
	}

	public createSession(authResult: AuthResult) {
		this.#store.dispatch(authActions.cREATE_SESSION({ authResult }))
	}

	public parseHash(hashStr: string): AuthResult {

		hashStr = hashStr.replace(/^#?\/?/, '');

		const result: AuthResult = {
			expiresAt: 0,
			nonce: undefined,
			state: undefined,
			idToken: undefined
		};

		if (hashStr.length > 0) {

			const tokens = hashStr.split('&');
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
		return result;
	}



}