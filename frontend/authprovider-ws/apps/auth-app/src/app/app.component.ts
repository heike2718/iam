import { Component, OnDestroy, OnInit } from '@angular/core';
import { LogService } from '@authprovider-ws/common-logging';
import { environment } from '../environments/environment';
import { VersionService } from './services/version.service';
import { AppData } from './shared/app-data.service';

@Component({
	// tslint:disable-next-line:component-selector
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

	version = environment.version;
	envName = environment.envName;
	showEnv = this.envName === 'DEV';
	api = environment.apiUrl;
	logo = environment.assetsUrl + '/mja_logo.png';
	innerWidth: number;

	
	constructor(private logger: LogService, public versionService: VersionService, public appData: AppData,) { }

	ngOnInit() {
		const location = window.location;
		const hash = window.location.href;
		this.logger.debug('location=' + location);
		this.logger.debug('hash=' + hash);
		this.innerWidth = window.innerWidth;

		this.logger.debug('innerWidth=' + this.innerWidth);
		this.versionService.ladeExpectedGuiVersion();
	}
}

