import {Component} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {NgcCookieConsentService} from "ngx-cookieconsent";
import {TranslationService} from "./services/translation.service";
import {filter, map, mergeMap} from "rxjs";
import {Title} from "@angular/platform-browser";

declare let gtag: Function;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})

export class AppComponent {

  appName = 'DorvakAuthServices';
  googleAnalyticsCode = 'UA-162370389-1'

  constructor(public router: Router, _cookieConsent: NgcCookieConsentService, _translate: TranslationService, private activatedRoute: ActivatedRoute, private titleService: Title) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        gtag('config', this.googleAnalyticsCode, {'page_path': event.urlAfterRedirects});
      }
    });
  }

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      map(() => this.activatedRoute),
      map(route => {
        while (route.firstChild) route = route.firstChild;
        return route;
      }),
      filter((route) => route.outlet === 'primary'),
      mergeMap((route) => route.data),
    ).subscribe(data => {
      let meta = data['meta'] || {}
      let title = meta['title'] || document.location.pathname.replace(/\//g, ' ').trim()
      title = title.charAt(0).toUpperCase() + title.slice(1)
      title = `${title} | ${this.appName}`
      this.titleService.setTitle(title)
    })
  }
}
