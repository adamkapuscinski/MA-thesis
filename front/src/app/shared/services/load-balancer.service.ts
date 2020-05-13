import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {SERVER_API_URL} from '../../app.constants';
import {Observable} from 'rxjs';
import {IAppRequestData} from "../model/app-request-data.model";

type MapResponseType = HttpResponse<IAppRequestData[]>;

@Injectable({ providedIn: 'root' })
export class LoadBalancerService {
    public resourceUrl = SERVER_API_URL + 'api/stats';

    constructor(private http: HttpClient) {}

    public stats(): Observable<MapResponseType> {
        return this.http.get<IAppRequestData[]>(this.resourceUrl, { observe: 'response' });
    }
}
