import {Component, OnInit, Renderer2} from '@angular/core';
import {TranslationService} from "../../services/translation.service";
import {NavigationService} from "../../services/navigation.service";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  enteredButton = false;
  isMatMenuOpen = false;
  isMatMenu2Open = false;
  prevButtonTrigger: { closeMenu: () => void; } | undefined;
  schema: SchemaDto[] = [
    {
      title: 'Home',
      path: '/'
    }
  ]

  constructor(private ren: Renderer2, router: Router, public translation: TranslationService, private navigationService: NavigationService, public userService: UserService) {

  }


  menuEnter() {
    this.isMatMenuOpen = true;
    if (this.isMatMenu2Open) {
      this.isMatMenu2Open = false;
    }
  }

  menuLeave(trigger: any, button: any) {
    setTimeout(() => {
      if (!this.isMatMenu2Open && !this.enteredButton) {
        this.isMatMenuOpen = false;
        trigger.closeMenu();
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-focused');
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-program-focused');
      } else {
        this.isMatMenuOpen = false;
      }
    }, 80)
  }

  menu2Enter() {
    this.isMatMenu2Open = true;
  }

  menu2Leave(trigger1: any, trigger2: any, button: any) {
    setTimeout(() => {
      if (this.isMatMenu2Open) {
        trigger1.closeMenu();
        this.isMatMenuOpen = false;
        this.isMatMenu2Open = false;
        this.enteredButton = false;
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-focused');
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-program-focused');
      } else {
        this.isMatMenu2Open = false;
        trigger2.closeMenu();
      }
    }, 100)
  }

  buttonEnter(trigger: any) {
    setTimeout(() => {
      if (this.prevButtonTrigger && this.prevButtonTrigger != trigger) {
        this.prevButtonTrigger.closeMenu();
        this.prevButtonTrigger = trigger;
        this.isMatMenuOpen = false;
        this.isMatMenu2Open = false;
        trigger.openMenu();
        if (trigger.menu.items.first) {
          this.ren.removeClass(trigger.menu.items.first['_elementRef'].nativeElement, 'cdk-focused');
          this.ren.removeClass(trigger.menu.items.first['_elementRef'].nativeElement, 'cdk-program-focused');
        }
      } else if (!this.isMatMenuOpen) {
        this.enteredButton = true;
        this.prevButtonTrigger = trigger
        trigger.openMenu();
        if (trigger.menu.items.first) {
          this.ren.removeClass(trigger.menu.items.first['_elementRef'].nativeElement, 'cdk-focused');
          this.ren.removeClass(trigger.menu.items.first['_elementRef'].nativeElement, 'cdk-program-focused');
        }
      } else {
        this.enteredButton = true;
        this.prevButtonTrigger = trigger
      }
    })
  }

  buttonLeave(trigger: any, button: any) {
    setTimeout(() => {
      if (this.enteredButton && !this.isMatMenuOpen) {
        trigger.closeMenu();
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-focused');
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-program-focused');
      }
      if (!this.isMatMenuOpen) {
        trigger.closeMenu();
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-focused');
        this.ren.removeClass(button['_elementRef'].nativeElement, 'cdk-program-focused');
      } else {
        this.enteredButton = false;
      }
    }, 100)
  }

  ngOnInit(): void {
  }

  resolveLink(item: SchemaDto | string) {
    let link = typeof item === "string" ? item : item.path

    if (link.startsWith("http")) {
      // external link
    } else if (link == 'home') {
      this.navigationService.navigate('/')
    } else {
      this.navigationService.navigate(link)
    }
  }

  login() {
    this.navigationService.navigate('/login?redirect=' + window.location.pathname)
  }
}

export interface SchemaDto {
  path: string
  title: string
  children?: SchemaDto[]
  data?: {
    icon?: string
    additionalClasses?: string
  }
}
