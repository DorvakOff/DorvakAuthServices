import { Component, OnInit } from '@angular/core';
import {NavigationService} from "../../services/navigation.service";

@Component({
  selector: 'app-error404',
  templateUrl: './error404.component.html',
  styleUrls: ['./error404.component.scss']
})
export class Error404Component implements OnInit {
  url: string = window.location.pathname;

  constructor(private navigationService: NavigationService) { }

  ngOnInit(): void {
  }

  redirect() {
    localStorage.setItem('redirect', '/dashboard');
    this.navigationService.navigate('/')
  }
}
