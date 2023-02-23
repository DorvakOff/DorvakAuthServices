import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AlertHandlerService} from "../../services/alert-handler.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  oldPassword: any;
  password: any;
  confirmPassword: any;

  constructor(private userService: UserService, private alertHandler: AlertHandlerService) {
  }

  ngOnInit(): void {
  }

  changePassword() {
    if (!this.oldPassword || !this.password || !this.confirmPassword) {
      this.alertHandler.raiseError('Please fill all fields')
      return
    }

    if (!/(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}/.test(this.password)) {
      this.alertHandler.raiseError('Password does not meet the requirements (at least 8 characters long, has digits, uppercase and lowercase letters, and special characters)')
      return
    }

    if (this.password !== this.confirmPassword) {
      this.alertHandler.raiseError('Passwords do not match')
      return
    }

    this.userService.changePassword(this.oldPassword, this.password).subscribe(() => {
      this.oldPassword = ''
      this.password = ''
      this.confirmPassword = ''
      this.alertHandler.sendSuccess('Account Updated', 'Your password has been changed')
    }, error => {
      this.alertHandler.raiseError(error)
    })
  }
}
