import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {LoadBalancerService} from './services/load-balancer.service';
import {SharedPrimengModule} from "./shared-primeng.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";

@NgModule({
    imports: [
      SharedPrimengModule,
      FontAwesomeModule
    ],
    providers: [
      LoadBalancerService
    ],
    exports: [
      SharedPrimengModule,
      FontAwesomeModule
    ]
})
export class SharedModule {}
