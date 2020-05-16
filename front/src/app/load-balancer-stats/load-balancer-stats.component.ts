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
  data: IChartDataModel;

  constructor(private loadBalancerService: LoadBalancerService) {
    // this.data = {
    //   labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    //   datasets: [
    //     {
    //       label: 'First Dataset',
    //       data: [65, 59, 80, 81, 56, 55, 40],
    //       fill: false,
    //       borderColor: '#4bc0c0'
    //     },
    //     {
    //       label: 'Second Dataset',
    //       data: [28, 48, 40, 19, 86, 27, 90],
    //       fill: false,
    //       borderColor: '#565656'
    //     }
    //   ]
    // }
  }

  ngOnInit() {
    this.loadBalancerService.stats().subscribe(res => {
      this.data = res.body;
      // this.statList = res.body;

      // const elements = new Set(this.statList.map(el => el.serviceIdentity));
      // for (let element of elements) {
      //   this.stats.set(element, this.statList.filter(el => el.serviceIdentity === element));
      // }
      // setTimeout(() => {
      //   this.dataAvailable = true;
      // }, 100);
    });
  }
}
