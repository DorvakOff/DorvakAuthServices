import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-button', templateUrl: './button.component.html', styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {

  @Input('type') type: 'danger' | 'info' | 'success' | 'outline-info' | 'outline-danger' | 'outline-wide-info' | 'outline-wide-danger' = 'info'
  @Input('disabled') disabled: boolean = false
  @Output('onClick') onClickEvent = new EventEmitter<MouseEvent>()

  constructor() {
  }

  getClass() {
    let classes = 'button '
    switch (this.type) {
      case 'danger':
        classes += 'button-danger'
        break
      case 'success':
        classes += 'button-success'
        break
      case "outline-info":
        classes += 'button-outline-info'
        break
      case "outline-danger":
        classes += 'button-outline-danger'
        break
      case 'outline-wide-info':
        classes += 'button-outline-info wide'
        break;
      case 'outline-wide-danger':
        classes += 'button-outline-danger wide'
        break;
      case 'info':
      default:
        classes += 'button-info'
        break
    }
    return classes
  }

  onClick($event: MouseEvent) {
    if (!this.disabled) this.onClickEvent.emit($event)
  }

  ngOnInit(): void {
  }

}
