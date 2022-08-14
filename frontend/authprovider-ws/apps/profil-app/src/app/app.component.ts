import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { environment } from '../environments/environment';
import { store } from './shared/store/app-data';
import { VersionService } from './services/version.service';

@Component({
	// tslint:disable-next-line:component-selector
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

	title = 'Minikänguru-Benutzerdaten';
	version = environment.version;
	envName = environment.envName;
	showEnv = environment.envName === 'DEV';
	api = environment.apiUrl;
	logo = environment.assetsUrl + '/mja_logo.png';

	constructor(private authService: AuthService, public versionService: VersionService) { }

	ngOnInit() {

		store.updateBlockingIndicator(false);

		this.versionService.ladeExpectedGuiVersion();

		// nach dem redirect vom AuthProvider ist das die Stelle, an der die Anwendung wieder ankommt.
		// Daher hier redirect-URL parsen
		const hash = window.location.hash;
		if (hash && hash.indexOf('idToken') > 0) {
			const authResult = this.authService.parseHash(hash);
			this.authService.createSession(authResult);
		}
	}
}
