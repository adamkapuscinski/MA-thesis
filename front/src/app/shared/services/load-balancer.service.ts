import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {SERVER_API_URL} from '../../app.constants';
import {Observable} from 'rxjs';
import {IChartDataModel, IChartSetData} from "../model/chart-datatype.model";

type ChartResponseType = HttpResponse<IChartSetData>;

@Injectable({ providedIn: 'root' })
export class LoadBalancerService {
    public resourceUrl = SERVER_API_URL + 'api/stats';

    constructor(private http: HttpClient) {}

    public stats(): Observable<ChartResponseType> {
        return this.http.get<IChartSetData>(this.resourceUrl, { observe: 'response' });
    }
    call(): Observable<HttpResponse<any>> {
      return this.http.get<any>('api/call', { observe: 'response' });
    }
}
