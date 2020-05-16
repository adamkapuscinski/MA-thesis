import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {LoadBalancerService} from './services/load-balancer.service';
import {SharedPrimengModule} from "./shared-primeng.module";

@NgModule({
    imports: [
      SharedPrimengModule
    ],
    providers: [
      LoadBalancerService
    ],
    exports: [
      SharedPrimengModule
    ]
})
export class SharedModule {}
