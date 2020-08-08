export interface IDefaultRequestTimeModel {
  applicationType?: string;
  requestTime?: number;
}
export class DefaultRequestTimeModel implements IDefaultRequestTimeModel {
  constructor(
    public applicationType?: string,
    public requestTime?: number
  ) {}
}
