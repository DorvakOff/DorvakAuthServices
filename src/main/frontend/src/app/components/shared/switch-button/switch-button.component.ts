import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-switch-button',
  templateUrl: './switch-button.component.html',
  styleUrls: ['./switch-button.component.scss']
})
export class SwitchButtonComponent implements OnInit {

  @Input("checked") checked: boolean = false

  constructor() {
  }

  @Input("onChange") onChange: ($event: Event) => void = () => {
  }

  ngOnInit(): void {
  }
}
