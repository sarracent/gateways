import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeripheral } from 'app/shared/model/peripheral.model';

@Component({
    selector: 'jhi-peripheral-detail',
    templateUrl: './peripheral-detail.component.html'
})
export class PeripheralDetailComponent implements OnInit {
    peripheral: IPeripheral;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ peripheral }) => {
            this.peripheral = peripheral;
        });
    }

    previousState() {
        window.history.back();
    }
}
