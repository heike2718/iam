import { Injectable } from "@angular/core";
import { CookieService } from "ngx-cookie-service";


@Injectable()
export class CommonCookiesService {


    constructor(private ngCookieService: CookieService) { }

    public readCSRFCookieValue(): string | undefined {

        const value = this.ngCookieService.get('XSRF-TOKEN');
        return value;
    }

}