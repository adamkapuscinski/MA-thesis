import {IDefaultRequestTimeModel} from "./default-request-time.model";

export interface ILoadBalancerConfigModel {
  callType?: string;
  methodType?: string;
  defaultAppsRequestTime?: IDefaultRequestTimeModel[],
  timeToShowInMinutes?: number,
  synchronizationTime?: number
}
export class LoadBalancerConfigModel implements ILoadBalancerConfigModel {
  constructor(
    public callType?: string,
    public methodType?: string,
    public defaultAppsRequestTime?: IDefaultRequestTimeModel[],
    public timeToShowInMinutes?: number,
    public synchronizationTime?: number
  ) {}
}
