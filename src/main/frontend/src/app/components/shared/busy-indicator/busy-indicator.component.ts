import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-busy-indicator',
  templateUrl: './busy-indicator.component.html',
  styleUrls: ['./busy-indicator.component.scss']
})
export class BusyIndicatorComponent implements OnInit {

  @Input("scope") scope: 'fixed' | 'relative' = 'fixed'
  @Input("size") size: string = '10rem'

  constructor() { }

  ngOnInit(): void {
  }

}
