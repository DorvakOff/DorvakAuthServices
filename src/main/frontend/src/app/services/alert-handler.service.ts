import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AlertHandlerService {

  alerts: Alert[] = [];

  constructor() {
  }

  public raiseError(error: any | string) {
    this.addAlert({
      severity: 'danger',
      title: 'An error occurred',
      body: typeof error === 'string' ? error : (error.error && error.error.message ? error.error.message : error.message)
    })
  }

  public sendSuccess(title: string, body: string) {
    this.addAlert({
      severity: 'success',
      title: title,
      body: body
    })
  }

  private addAlert(alert: Alert) {
    this.alerts.push(alert)
    setTimeout(() => {
      this.alerts.shift()
    }, 5000)
  }
}

export interface Alert {
  severity: 'primary' | 'danger' | 'success' | 'warning'
  title: string
  body: string
  additionalInfos?: string
}
