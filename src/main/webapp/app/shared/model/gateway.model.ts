import { IPeripheral } from 'app/shared/model/peripheral.model';

export interface IGateway {
    id?: number;
    serialNumber?: string;
    humanRadableName?: string;
    iPVFour?: string;
    peripherals?: IPeripheral[];
}

export class Gateway implements IGateway {
    constructor(
        public id?: number,
        public serialNumber?: string,
        public humanRadableName?: string,
        public iPVFour?: string,
        public peripherals?: IPeripheral[]
    ) {}
}
