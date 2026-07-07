import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTodoForm } from './add-todo-form';

describe('AddTodoForm', () => {
  let component: AddTodoForm;
  let fixture: ComponentFixture<AddTodoForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddTodoForm],
    }).compileComponents();

    fixture = TestBed.createComponent(AddTodoForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
