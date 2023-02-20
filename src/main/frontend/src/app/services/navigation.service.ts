import {Injectable} from '@angular/core';
import {Router} from "@angular/router";
import {Title} from "@angular/platform-browser";

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(private router: Router, private titleService: Title) {
  }

  navigate(url: string) {
    this.navigateWithProps(url, {})
  }

  navigateWithProps(url: string, props: {}) {
    this.router.navigateByUrl(url, {state: props})
  }
}
