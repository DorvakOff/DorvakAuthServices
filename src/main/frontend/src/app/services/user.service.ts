import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {TranslationService} from "./translation.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  user: any

  constructor(private http: HttpClient, private translationService: TranslationService) {
  }

  login(loginEmail: any, loginPassword: any): Observable<any> {
    this.user = {
      name: 'John Doe',
      email: '',
      phone: '',
      id: '1234567890',
      avatarUrl: "https://i.pravatar.cc/300"
    }
    return this.http.post<any>('/api/user/login', {
      email: loginEmail,
      password: loginPassword
    })
  }

  logout() {
    this.user = undefined;
  }

  changePassword(oldPassword: any, password: any): Observable<any> {
    return this.http.post<any>('/api/user/change-password', {
      oldPassword: oldPassword,
      password: password
    })
  }

  createAccount(email: any, password: any, username: any): Observable<any> {
    return this.http.post<any>('/api/user/create-account', {
      email: email,
      password: password,
      username: username,
      language: this.translationService.getLang()
    })
  }
}
