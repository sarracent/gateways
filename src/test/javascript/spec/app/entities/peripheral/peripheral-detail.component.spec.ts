/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewaysTestModule } from '../../../test.module';
import { PeripheralDetailComponent } from 'app/entities/peripheral/peripheral-detail.component';
import { Peripheral } from 'app/shared/model/peripheral.model';

describe('Component Tests', () => {
    describe('Peripheral Management Detail Component', () => {
        let comp: PeripheralDetailComponent;
        let fixture: ComponentFixture<PeripheralDetailComponent>;
        const route = ({ data: of({ peripheral: new Peripheral(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GatewaysTestModule],
                declarations: [PeripheralDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PeripheralDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PeripheralDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.peripheral).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
