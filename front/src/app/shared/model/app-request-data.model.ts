export interface IAppRequestData {
    start?: Date,
    serviceIdentity?: string,
    finish?: Date,
    duration?: number
}
export class AppRequestData implements IAppRequestData {
    constructor(
        public start?: Date,
        public serviceIdentity?: string,
        public finish?: Date,
        public duration?: number,
    ) {}
}
