import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IPeripheral } from 'app/shared/model/peripheral.model';
import { PeripheralService } from './peripheral.service';
import { IGateway } from 'app/shared/model/gateway.model';
import { GatewayService } from 'app/entities/gateway';

@Component({
    selector: 'jhi-peripheral-update',
    templateUrl: './peripheral-update.component.html'
})
export class PeripheralUpdateComponent implements OnInit {
    peripheral: IPeripheral;
    isSaving: boolean;

    gateways: IGateway[];
    dateCreatedDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private peripheralService: PeripheralService,
        private gatewayService: GatewayService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ peripheral }) => {
            this.peripheral = peripheral;
        });
        this.gatewayService.query().subscribe(
            (res: HttpResponse<IGateway[]>) => {
                this.gateways = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.peripheral.id !== undefined) {
            this.subscribeToSaveResponse(this.peripheralService.update(this.peripheral));
        } else {
            this.subscribeToSaveResponse(this.peripheralService.create(this.peripheral));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPeripheral>>) {
        result.subscribe((res: HttpResponse<IPeripheral>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackGatewayById(index: number, item: IGateway) {
        return item.id;
    }
}
