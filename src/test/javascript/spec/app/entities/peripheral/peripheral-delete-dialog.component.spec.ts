/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GatewaysTestModule } from '../../../test.module';
import { PeripheralDeleteDialogComponent } from 'app/entities/peripheral/peripheral-delete-dialog.component';
import { PeripheralService } from 'app/entities/peripheral/peripheral.service';

describe('Component Tests', () => {
    describe('Peripheral Management Delete Component', () => {
        let comp: PeripheralDeleteDialogComponent;
        let fixture: ComponentFixture<PeripheralDeleteDialogComponent>;
        let service: PeripheralService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GatewaysTestModule],
                declarations: [PeripheralDeleteDialogComponent]
            })
                .overrideTemplate(PeripheralDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PeripheralDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeripheralService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
