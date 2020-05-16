import { NgModule } from '@angular/core';
import {ButtonModule} from "primeng/button";
import 'chart.js/dist/Chart.min.js';
import {ChartModule} from "primeng/chart";

@NgModule({
  imports: [
    ChartModule,
    ButtonModule
  ],
  exports: [
    ChartModule,
    ButtonModule
  ]
})
export class SharedPrimengModule {}
