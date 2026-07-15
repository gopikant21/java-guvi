import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Profile } from './profile';
import { Token } from '../../../../features/auth/services/token';
import { Customer } from '../../services/customer';
import { of } from 'rxjs';

describe('Profile', () => {
  let component: Profile;
  let fixture: ComponentFixture<Profile>;
  let customerService: jasmine.SpyObj<Customer>;
  let tokenService: jasmine.SpyObj<Token>;

  beforeEach(async () => {
    const customerSpy = jasmine.createSpyObj('Customer', ['getCustomerByEmail', 'updateCustomer']);
    const tokenSpy = jasmine.createSpyObj('Token', ['getUsername']);

    await TestBed.configureTestingModule({
      imports: [Profile, ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        { provide: Customer, useValue: customerSpy },
        { provide: Token, useValue: tokenSpy }
      ]
    }).compileComponents();

    customerService = TestBed.inject(Customer) as jasmine.SpyObj<Customer>;
    tokenService = TestBed.inject(Token) as jasmine.SpyObj<Token>;
    tokenService.getUsername.and.returnValue('testuser');

    fixture = TestBed.createComponent(Profile);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch profile on init', () => {
    const mockProfile = { id: 1, name: 'Test User', email: 'test@example.com' };
    customerService.getCustomerByEmail.and.returnValue(of(mockProfile));

    fixture.detectChanges();

    expect(customerService.getCustomerByEmail).toHaveBeenCalledWith('testuser');
    expect(component.profile()).toEqual(mockProfile);
  });

  it('should handle form submission with valid data', () => {
    const mockProfile = { id: 1, name: 'Test', email: 'test@example.com', phone: '', address: '' };
    const updatedProfile = { ...mockProfile, name: 'Updated' };
    customerService.getCustomerByEmail.and.returnValue(of(mockProfile));
    customerService.updateCustomer.and.returnValue(of(updatedProfile));

    fixture.detectChanges();
    component.form.patchValue({ name: 'Updated' });
    component.onSubmit();

    expect(customerService.updateCustomer).toHaveBeenCalled();
  });
});
