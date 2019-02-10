/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GatewaysTestModule } from '../../../test.module';
import { PeripheralUpdateComponent } from 'app/entities/peripheral/peripheral-update.component';
import { PeripheralService } from 'app/entities/peripheral/peripheral.service';
import { Peripheral } from 'app/shared/model/peripheral.model';

describe('Component Tests', () => {
    describe('Peripheral Management Update Component', () => {
        let comp: PeripheralUpdateComponent;
        let fixture: ComponentFixture<PeripheralUpdateComponent>;
        let service: PeripheralService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GatewaysTestModule],
                declarations: [PeripheralUpdateComponent]
            })
                .overrideTemplate(PeripheralUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PeripheralUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeripheralService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Peripheral(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.peripheral = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Peripheral();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.peripheral = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
