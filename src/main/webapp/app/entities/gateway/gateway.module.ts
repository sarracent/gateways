import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaysSharedModule } from 'app/shared';
import {
    GatewayComponent,
    GatewayDetailComponent,
    GatewayUpdateComponent,
    GatewayDeletePopupComponent,
    GatewayDeleteDialogComponent,
    gatewayRoute,
    gatewayPopupRoute
} from './';

const ENTITY_STATES = [...gatewayRoute, ...gatewayPopupRoute];

@NgModule({
    imports: [GatewaysSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GatewayComponent,
        GatewayDetailComponent,
        GatewayUpdateComponent,
        GatewayDeleteDialogComponent,
        GatewayDeletePopupComponent
    ],
    entryComponents: [GatewayComponent, GatewayUpdateComponent, GatewayDeleteDialogComponent, GatewayDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewaysGatewayModule {}
