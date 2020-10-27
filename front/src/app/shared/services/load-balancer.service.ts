import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {IChartSetData} from "../model/chart-datatype.model";
import {ILoadBalancerConfigModel} from "../model/load-balancer-config.model";

type ChartResponseType = HttpResponse<IChartSetData>;

@Injectable({ providedIn: 'root' })
export class LoadBalancerService {

    constructor(private http: HttpClient) {}

    public stats(): Observable<ChartResponseType> {
        return this.http.get<IChartSetData>('api/stats', { observe: 'response' });
    }
    call(): Observable<HttpResponse<any>> {
      return this.http.get<any>('api/call', { observe: 'response' });
    }
    assignParameters(config: any): Observable<HttpResponse<ILoadBalancerConfigModel>> {
      return this.http.post<ILoadBalancerConfigModel>('api/assign-params', config, {observe: 'response'});
    }
}
