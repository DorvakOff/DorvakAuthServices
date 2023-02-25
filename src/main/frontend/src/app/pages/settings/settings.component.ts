import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AlertHandlerService} from "../../services/alert-handler.service";
@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  oldPassword: any
  password: any
  confirmPassword: any
  loading: boolean = false

  constructor(private userService: UserService, private alertHandler: AlertHandlerService) {
  }

  ngOnInit(): void {
  }

  changePassword() {
    this.loading = true
    if (!this.oldPassword || !this.password || !this.confirmPassword) {
      this.alertHandler.raiseError('Please fill all fields')
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

    this.userService.changePassword(this.oldPassword, this.password).subscribe(() => {
      this.oldPassword = ''
      this.password = ''
      this.confirmPassword = ''
      this.alertHandler.sendSuccess('Account Updated', 'Your password has been changed')
      this.loading = false
    }, error => {
      this.loading = false
      this.alertHandler.raiseError(error)
    })
  }

  onAvatarFileSubmit($event: Event) {
    if (!$event.target) return
    let target = ($event.target as HTMLInputElement)
    if (!target.files) return
    let file = target.files[0]
    if (!file) return
    this.loading = true
    this.userService.changeAvatar(file).subscribe(() => {
      this.alertHandler.sendSuccess('Account Updated', 'Your avatar has been changed')
      if (this.userService.user) {
        this.userService.user.avatar = this.userService.user.avatar.substring(0, this.userService.user.avatar.lastIndexOf('/') + 1) + file.name
      }
      this.loading = false
    }, error => {
      this.loading = false
      this.alertHandler.raiseError(error)
    })
  }
}
