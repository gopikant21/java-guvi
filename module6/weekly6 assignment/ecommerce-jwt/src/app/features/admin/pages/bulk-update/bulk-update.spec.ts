import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkUpdate } from './bulk-update';

describe('BulkUpdate', () => {
  let component: BulkUpdate;
  let fixture: ComponentFixture<BulkUpdate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BulkUpdate],
    }).compileComponents();

    fixture = TestBed.createComponent(BulkUpdate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
