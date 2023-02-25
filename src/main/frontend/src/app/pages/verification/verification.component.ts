import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AlertHandlerService} from "../../services/alert-handler.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-verification',
  templateUrl: './verification.component.html',
  styleUrls: ['./verification.component.scss']
})
export class VerificationComponent implements OnInit {
  loading: boolean = false
  token: string = ''
  verified: boolean = false

  constructor(private userService: UserService, private alertHandler: AlertHandlerService, public activatedRoute: ActivatedRoute) {
    activatedRoute.paramMap.subscribe(params => {
      let token = params.get('token')
      console.log(token)
      if (token) {
        this.token = token
      }
      this.loading = true
      this.userService.verifyEmail(this.token).subscribe(() => {
        this.loading = false
        this.verified = true
      }, () => {
        this.loading = false
        this.alertHandler.raiseError('Verification failed, the token is invalid or expired')
      })
    })
  }

  ngOnInit(): void {
  }

}
