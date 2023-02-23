import {Component, OnInit} from '@angular/core';
import {NavigationService} from "../../services/navigation.service";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-error404',
  templateUrl: './error404.component.html',
  styleUrls: ['./error404.component.scss']
})
export class Error404Component implements OnInit {
  url: string = window.location.pathname;

  constructor(private navigationService: NavigationService, private router: Router) {
    router.events.subscribe((val) => {
      if (val instanceof NavigationEnd) {
        this.url = val.url;
      }
    });
  }

  ngOnInit(): void {
    this.url = window.location.pathname;
  }

  redirect() {
    localStorage.setItem('redirect', '/dashboard');
    this.navigationService.navigate('/')
  }
}
