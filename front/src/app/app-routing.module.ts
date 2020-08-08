import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoadBalancerStatsComponent} from "./load-balancer-stats/load-balancer-stats.component";


const routes: Routes = [
  { path: '', component: LoadBalancerStatsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
