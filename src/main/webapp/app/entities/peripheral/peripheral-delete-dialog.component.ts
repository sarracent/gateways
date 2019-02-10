import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeripheral } from 'app/shared/model/peripheral.model';
import { PeripheralService } from './peripheral.service';

@Component({
    selector: 'jhi-peripheral-delete-dialog',
    templateUrl: './peripheral-delete-dialog.component.html'
})
export class PeripheralDeleteDialogComponent {
    peripheral: IPeripheral;

    constructor(private peripheralService: PeripheralService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.peripheralService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'peripheralListModification',
                content: 'Deleted an peripheral'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-peripheral-delete-popup',
    template: ''
})
export class PeripheralDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ peripheral }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PeripheralDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.peripheral = peripheral;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
