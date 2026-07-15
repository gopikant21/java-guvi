import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { CustomerDashboard } from './customer-dashboard';
import { Dashboard } from '../../../../core/services/dashboard';
import { Token } from '../../../auth/services/token';

const dashboardMock = {
  getCustomerOverview: () =>
    of({
      totalOrders: 0,
      activeOrders: 0,
      deliveredOrders: 0,
      cancelledOrders: 0,
      totalSpent: 0,
      recentOrders: [],
      availableProductCount: 0,
      featuredProducts: [],
    }),
};

const tokenMock = {
  getUsername: () => '',
};

describe('CustomerDashboard', () => {
  let component: CustomerDashboard;
  let fixture: ComponentFixture<CustomerDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerDashboard],
      providers: [
        provideRouter([]),
        { provide: Dashboard, useValue: dashboardMock },
        { provide: Token, useValue: tokenMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
