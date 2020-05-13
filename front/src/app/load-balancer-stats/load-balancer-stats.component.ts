import { Component, OnInit } from '@angular/core';
import {LoadBalancerService} from "../shared/services/load-balancer.service";
import {IAppRequestData} from "../shared/model/app-request-data.model";

@Component({
  selector: 'app-load-balancer-stats',
  templateUrl: './load-balancer-stats.component.html',
  styleUrls: ['./load-balancer-stats.component.scss']
})
export class LoadBalancerStatsComponent implements OnInit {
  statList: IAppRequestData[] = [];
  stats: Map<string, IAppRequestData[]> = new Map<string, IAppRequestData[]>();
  dataAvailable = false;
  constructor(private loadBalancerService: LoadBalancerService) { }

  ngOnInit() {
    this.loadBalancerService.stats().subscribe(res => {
      this.statList = res.body;

      const elements = new Set(this.statList.map(el => el.serviceIdentity));
      for (let element of elements) {
        this.stats.set(element, this.statList.filter(el => el.serviceIdentity === element));
      }
      setTimeout(() => {
        this.dataAvailable = true;
      }, 100);
    });
  }
}
