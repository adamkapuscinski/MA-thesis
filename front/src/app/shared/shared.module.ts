import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {LoadBalancerService} from './services/load-balancer.service';

@NgModule({
    providers: [
        LoadBalancerService
    ],
})
export class SharedModule {}
