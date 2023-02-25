import {Component, OnInit} from '@angular/core';
import {AlertHandlerService} from "../../services/alert-handler.service";
import {UserService} from "../../services/user.service";
import {NavigationService} from "../../services/navigation.service";
import {ActivatedRoute} from "@angular/router";
import {TranslationService} from "../../services/translation.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  email: any
  password: any
  username: any
  confirmPassword: any
  loginEmail: any
  loginPassword: any
  redirectUrl: string = '/'
  loading = false

  constructor(private translationService: TranslationService, private alertHandler: AlertHandlerService, private userService: UserService, private navigationService: NavigationService, activatedRoute: ActivatedRoute) {
    activatedRoute.queryParams.subscribe(params => {
      this.redirectUrl = params['redirect'] || '/'
      if (this.redirectUrl === '/login') {
        this.redirectUrl = '/'
      }
      if (userService.user) {
        this.navigationService.navigate(this.redirectUrl)
      }
    })
  }

  ngOnInit(): void {
  }

  login() {
    this.loading = true
    if (!this.loginEmail || !this.loginPassword) {
      this.alertHandler.raiseError('Please fill in all fields')
      this.loading = false
      return
    }

    this.userService.login(this.loginEmail, this.loginPassword).subscribe(token => {
      this.userService.token = token
      localStorage.setItem('token', token)
      this.userService.getUser().subscribe(user => {
        this.userService.user = user
        this.alertHandler.sendSuccess('Login successful', 'You have been logged in successfully')
        this.translationService.changeLang(user.language)
        this.navigationService.navigate(this.redirectUrl)
        this.loading = false
      }, error => {
        this.loading = false
        this.alertHandler.raiseError(error)
      })
    }, error => {
      this.loading = false
      if (error.status === 401) {
        this.alertHandler.raiseError('Invalid credentials')
      } else {
        this.alertHandler.raiseError(error)
      }
    })
  }

  createAccount() {
    this.loading = true
    if (!this.email || !this.password || !this.username || !this.confirmPassword) {
      this.alertHandler.raiseError('Please fill in all fields')
      this.loading = false
      return
    }

    if (!/(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}/.test(this.password)) {
      this.alertHandler.raiseError('Password does not meet the requirements (at least 8 characters long, has digits, uppercase and lowercase letters, and special characters)')
      this.loading = false
      return
    }

    if (this.password !== this.confirmPassword) {
      this.alertHandler.raiseError('Passwords do not match')
      this.loading = false
      return
    }

    this.userService.createAccount(this.email, this.password, this.username).subscribe(token => {
      this.userService.token = token
      localStorage.setItem('token', token)
      this.userService.getUser().subscribe(user => {
        this.userService.user = user
        this.translationService.changeLang(user.language)
        this.alertHandler.sendSuccess('Account created', 'Your account has been created successfully')
        this.navigationService.navigate(this.redirectUrl)
        this.loading = false
      }, error => {
        this.loading = false
        this.alertHandler.raiseError(error)
      })
    }, error => {
      this.loading = false
      this.alertHandler.raiseError(error)
    })
  }
}
