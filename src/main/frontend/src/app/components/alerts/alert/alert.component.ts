import {Component, Input, OnInit} from '@angular/core';

@Component({
    selector: 'app-alert',
    templateUrl: './alert.component.html',
    styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnInit {

    @Input("severity") severity: 'primary' | 'danger' | 'success' | 'warning' = 'primary'
    @Input("title") title?: string
    @Input("body") body?: string
    @Input("additionalInfos") additionalInfos?: string

    constructor() {
    }

    ngOnInit(): void {
    }
}
