import { Moment } from 'moment';
import { IGateway } from 'app/shared/model//gateway.model';

export const enum Status {
    ONLINE = 'ONLINE',
    OFFLINE = 'OFFLINE'
}

export interface IPeripheral {
    id?: number;
    uID?: number;
    vendor?: string;
    dateCreated?: Moment;
    status?: Status;
    gateway?: IGateway;
}

export class Peripheral implements IPeripheral {
    constructor(
        public id?: number,
        public uID?: number,
        public vendor?: string,
        public dateCreated?: Moment,
        public status?: Status,
        public gateway?: IGateway
    ) {}
}
