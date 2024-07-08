import { Component, OnDestroy, OnInit } from "@angular/core";
import { LoadingIndicatorService } from "./loading-indicator.service";
import { Subscription } from "rxjs";


@Component({
	selector: 'auth-loading-indicator',
	templateUrl: './loading-indicator.component.html',
	styleUrls: ['./loading-indicator.component.css']
})
export class LoadingIndicatorComponent implements OnInit, OnDestroy {

	showLoadingIndicator = false;

	private loadingSubscription: Subscription = new Subscription();

    constructor(private loadingIndicatorService: LoadingIndicatorService){
		console.log('loading indicator component');
	}

	ngOnInit(): void {
		this.loadingSubscription = this.loadingIndicatorService.loading$.subscribe((loading) => this.showLoadingIndicator = loading)
	}
	ngOnDestroy(): void {
		this.loadingSubscription.unsubscribe();		
	}

}