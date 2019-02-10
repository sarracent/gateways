import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Peripheral } from 'app/shared/model/peripheral.model';
import { PeripheralService } from './peripheral.service';
import { PeripheralComponent } from './peripheral.component';
import { PeripheralDetailComponent } from './peripheral-detail.component';
import { PeripheralUpdateComponent } from './peripheral-update.component';
import { PeripheralDeletePopupComponent } from './peripheral-delete-dialog.component';
import { IPeripheral } from 'app/shared/model/peripheral.model';

@Injectable({ providedIn: 'root' })
export class PeripheralResolve implements Resolve<IPeripheral> {
    constructor(private service: PeripheralService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Peripheral> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Peripheral>) => response.ok),
                map((peripheral: HttpResponse<Peripheral>) => peripheral.body)
            );
        }
        return of(new Peripheral());
    }
}

export const peripheralRoute: Routes = [
    {
        path: 'peripheral',
        component: PeripheralComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'gatewaysApp.peripheral.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'peripheral/:id/view',
        component: PeripheralDetailComponent,
        resolve: {
            peripheral: PeripheralResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewaysApp.peripheral.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'peripheral/new',
        component: PeripheralUpdateComponent,
        resolve: {
            peripheral: PeripheralResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewaysApp.peripheral.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'peripheral/:id/edit',
        component: PeripheralUpdateComponent,
        resolve: {
            peripheral: PeripheralResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewaysApp.peripheral.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const peripheralPopupRoute: Routes = [
    {
        path: 'peripheral/:id/delete',
        component: PeripheralDeletePopupComponent,
        resolve: {
            peripheral: PeripheralResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewaysApp.peripheral.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
