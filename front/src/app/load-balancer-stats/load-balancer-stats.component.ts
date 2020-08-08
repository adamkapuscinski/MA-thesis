import {Component, OnInit, TemplateRef} from '@angular/core';
import {LoadBalancerService} from "../shared/services/load-balancer.service";
import {IAppRequestData} from "../shared/model/app-request-data.model";
import {IChartDataModel} from "../shared/model/chart-datatype.model";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {ILoadBalancerConfigModel, LoadBalancerConfigModel} from "../shared/model/load-balancer-config.model";
import {DefaultRequestTimeModel, IDefaultRequestTimeModel} from "../shared/model/default-request-time.model";

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
  config: ILoadBalancerConfigModel;
  tempConfig: ILoadBalancerConfigModel;
  ESTABLISHED_PARAMS = 'ESTABLISHED_PARAMS';
  RANDOM_MATRIX_TRANSPOSE = 'RANDOM_MATRIX_TRANSPOSE';
  AVERAGE_RESPONSE_TIME = 'AVERAGE_RESPONSE_TIME';
  SEQUENTIALLY = 'SEQUENTIALLY';

  constructor(private loadBalancerService: LoadBalancerService, private modalService: NgbModal) {}

  ngOnInit() {
    this.initLoadBalancerConfig();
    this.loadStats();
  }
  initLoadBalancerConfig() {
    this.config = new LoadBalancerConfigModel();
    this.config.defaultAppsRequestTime = [new DefaultRequestTimeModel("SERVICEONE", 100), new DefaultRequestTimeModel("SERVICETWO", 200), new DefaultRequestTimeModel("SERVICETHREE", 500)];
    this.config.callType = 'ESTABLISHED_PARAMS';
    this.config.methodType = 'AVERAGE_RESPONSE_TIME';
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
  openModal(target: TemplateRef<any>, sizeFlag: boolean) {
    let options: NgbModalOptions = {};
    // backdrop: 'static'
    if (sizeFlag) {
      options['size'] = 'lg';
      options['windowClass'] = 'full-width-modal';
    }
    this.modalService.open(target, options);
  }
  assignParameters() {
    this.config = this.tempConfig;
    this.loadBalancerService.assignParameters(this.config)
      .subscribe(() => {
        this.loadStats();
      });
  }
  assignTempConfig() {
    this.tempConfig = JSON.parse(JSON.stringify(this.config));
  }
  trackId(index: number, item: IDefaultRequestTimeModel) {
    return item.applicationType;
  }
}
