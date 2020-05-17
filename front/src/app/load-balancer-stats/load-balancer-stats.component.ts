import { Component, OnInit } from '@angular/core';
import {LoadBalancerService} from "../shared/services/load-balancer.service";
import {IAppRequestData} from "../shared/model/app-request-data.model";
import {IChartDataModel} from "../shared/model/chart-datatype.model";

@Component({
  selector: 'app-load-balancer-stats',
  templateUrl: './load-balancer-stats.component.html',
  styleUrls: ['./load-balancer-stats.component.scss']
})
export class LoadBalancerStatsComponent implements OnInit {
  stats: Map<string, IAppRequestData[]> = new Map<string, IAppRequestData[]>();
  firstSet: IChartDataModel;
  secondSet: IChartDataModel;
  calling = false;
  loadingStats = false;

  constructor(private loadBalancerService: LoadBalancerService) {}

  ngOnInit() {
    this.loadStats();
  }
  callLoadBalancer() {
    this.calling = true;
    this.loadBalancerService.call().subscribe(() => {
      this.calling = false;
      this.loadStats();
    });
  }
  loadStats() {
    this.loadingStats = true;
    this.loadBalancerService.stats().subscribe(res => {
      this.loadingStats = false;
      this.firstSet = undefined;
      this.secondSet = undefined;
      setTimeout(() => {
        this.firstSet = res.body.firstSet;
        this.secondSet = res.body.secondSet;
      });
    });
  }
}
