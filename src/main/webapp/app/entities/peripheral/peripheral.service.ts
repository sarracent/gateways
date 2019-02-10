import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPeripheral } from 'app/shared/model/peripheral.model';

type EntityResponseType = HttpResponse<IPeripheral>;
type EntityArrayResponseType = HttpResponse<IPeripheral[]>;

@Injectable({ providedIn: 'root' })
export class PeripheralService {
    public resourceUrl = SERVER_API_URL + 'api/peripherals';

    constructor(private http: HttpClient) {}

    create(peripheral: IPeripheral): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(peripheral);
        return this.http
            .post<IPeripheral>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(peripheral: IPeripheral): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(peripheral);
        return this.http
            .put<IPeripheral>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPeripheral>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPeripheral[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(peripheral: IPeripheral): IPeripheral {
        const copy: IPeripheral = Object.assign({}, peripheral, {
            dateCreated:
                peripheral.dateCreated != null && peripheral.dateCreated.isValid() ? peripheral.dateCreated.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((peripheral: IPeripheral) => {
                peripheral.dateCreated = peripheral.dateCreated != null ? moment(peripheral.dateCreated) : null;
            });
        }
        return res;
    }
}
