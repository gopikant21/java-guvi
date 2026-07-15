import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { AdminDashboard } from './admin-dashboard';
import { Dashboard } from '../../../../core/services/dashboard';

const dashboardMock = {
  getAdminOverview: () =>
    of({
      totalProducts: 0,
      totalCustomers: 0,
      totalOrders: 0,
      revenue: 0,
      pendingOrders: 0,
      processingOrders: 0,
      shippedOrders: 0,
      deliveredOrders: 0,
      cancelledOrders: 0,
      lowStockProducts: [],
      emptyOrders: [],
      highValueOrders: [],
    }),
};

describe('AdminDashboard', () => {
  let component: AdminDashboard;
  let fixture: ComponentFixture<AdminDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminDashboard],
      providers: [{ provide: Dashboard, useValue: dashboardMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
