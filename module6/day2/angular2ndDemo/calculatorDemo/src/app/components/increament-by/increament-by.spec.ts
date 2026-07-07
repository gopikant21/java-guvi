import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncreamentBy } from './increament-by';

describe('IncreamentBy', () => {
  let component: IncreamentBy;
  let fixture: ComponentFixture<IncreamentBy>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncreamentBy],
    }).compileComponents();

    fixture = TestBed.createComponent(IncreamentBy);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
