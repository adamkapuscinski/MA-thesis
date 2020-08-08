import {IDefaultRequestTimeModel} from "./default-request-time.model";

export interface ILoadBalancerConfigModel {
  callType?: string;
  methodType?: string;
  defaultAppsRequestTime?: IDefaultRequestTimeModel[]
}
export class LoadBalancerConfigModel implements ILoadBalancerConfigModel {
  constructor(
    public callType?: string,
    public methodType?: string,
    public defaultAppsRequestTime?: IDefaultRequestTimeModel[]
  ) {}
}
