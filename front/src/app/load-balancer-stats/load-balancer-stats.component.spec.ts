import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadBalancerStatsComponent } from './load-balancer-stats.component';

describe('LoadBalancerStatsComponent', () => {
  let component: LoadBalancerStatsComponent;
  let fixture: ComponentFixture<LoadBalancerStatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadBalancerStatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadBalancerStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
