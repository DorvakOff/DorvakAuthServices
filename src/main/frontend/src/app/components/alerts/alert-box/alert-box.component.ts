import {Component, OnInit} from '@angular/core';
import {AlertHandlerService} from "../../../services/alert-handler.service";

@Component({
  selector: 'app-alert-box',
  templateUrl: './alert-box.component.html',
  styleUrls: ['./alert-box.component.scss']
})
export class AlertBoxComponent implements OnInit {

  constructor(public alertHandler: AlertHandlerService) {
  }

  ngOnInit(): void {
  }

}

