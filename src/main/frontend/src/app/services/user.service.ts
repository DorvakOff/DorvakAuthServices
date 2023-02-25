import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {TranslationService} from "./translation.service";
import {User} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  user?: User
  token?: string
  autoLoginLoading: boolean = false

  constructor(private http: HttpClient, private translationService: TranslationService) {
    this.token = localStorage.getItem('token') || undefined
    this.autoLogin()
  }

  login(loginEmail: any, loginPassword: any): Observable<string> {
    return this.http.post<string>('/api/user/login', {
        email: loginEmail,
        password: loginPassword
      }, {responseType: "text" as "json"}
    )
  }

  logout() {
    this.user = undefined;
    this.token = undefined;
    localStorage.removeItem('token')
  }

  getUser(): Observable<User> {
    return this.http.get<User>('/api/user/data', {
      headers: {
        'Authorization': 'Bearer ' + this.token
      }
    })
  }

  changePassword(oldPassword: any, password: any): Observable<any> {
    return this.http.patch<any>('/api/user/change-password', {
      oldPassword: oldPassword,
      password: password
    }, {
      headers: {
        'Authorization': 'Bearer ' + this.token
      }
    })
  }

  createAccount(email: any, password: any, username: any): Observable<string> {
    return this.http.post<string>('/api/user/create-account', {
      email: email,
      password: password,
      username: username,
      language: this.translationService.getLang()
    }, {responseType: "text" as "json"})
  }

  autoLogin() {
    if (this.token) {
      this.autoLoginLoading = true
      this.getUser().subscribe(user => {
        this.user = user
        this.autoLoginLoading = false
        this.translationService.changeLang(user.language)
      }, () => {
        this.autoLoginLoading = false
        this.token = undefined
        localStorage.removeItem('token')
      })
    }
  }

  verifyEmail(token: string): Observable<any> {
    return this.http.get<any>(`/api/verify/${token}`)
  }

  changeAvatar(file: File): Observable<any> {
    let formData = new FormData()
    formData.append('avatar', file)
    return this.http.patch<any>('/api/user/avatar', formData, {
      headers: {
        'Authorization': 'Bearer ' + this.token
      }
    })
  }

  changeLanguage(language: string): Observable<any> {
    return this.http.patch<any>('/api/user/update-language', {
      language: language
    }, {
      headers: {
        'Authorization': 'Bearer ' + this.token
      }
    })
  }
}
