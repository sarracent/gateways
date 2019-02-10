import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaysSharedModule } from 'app/shared';
import {
    PeripheralComponent,
    PeripheralDetailComponent,
    PeripheralUpdateComponent,
    PeripheralDeletePopupComponent,
    PeripheralDeleteDialogComponent,
    peripheralRoute,
    peripheralPopupRoute
} from './';

const ENTITY_STATES = [...peripheralRoute, ...peripheralPopupRoute];

@NgModule({
    imports: [GatewaysSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PeripheralComponent,
        PeripheralDetailComponent,
        PeripheralUpdateComponent,
        PeripheralDeleteDialogComponent,
        PeripheralDeletePopupComponent
    ],
    entryComponents: [PeripheralComponent, PeripheralUpdateComponent, PeripheralDeleteDialogComponent, PeripheralDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewaysPeripheralModule {}
