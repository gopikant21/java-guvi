import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Flight } from './flight.model';
import { FlightService } from './flight.service';

@Component({
  selector: 'app-flight-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './flight-dashboard.component.html',
  styleUrl: './flight-dashboard.component.css'
})
export class FlightDashboardComponent {
  flights: Flight[] = [];
  formData: Flight = this.createEmptyFlight();

  isEditMode = false;
  submitted = false;
  message = '';
  messageType: 'success' | 'error' = 'success';

  private editingOriginalId: number | null = null;

  constructor(private readonly flightService: FlightService) {
    this.loadFlights();
  }

  onSubmit(form: NgForm): void {
    this.submitted = true;

    if (form.invalid) {
      this.showMessage('Please fix validation errors before submitting.', 'error');
      return;
    }

    const payload: Flight = {
      flightId: Number(this.formData.flightId),
      airline: this.formData.airline.trim(),
      source: this.formData.source.trim(),
      destination: this.formData.destination.trim(),
      price: Number(this.formData.price),
      availableSeats: Number(this.formData.availableSeats)
    };

    if (this.isEditMode && this.editingOriginalId !== null) {
      const updated = this.flightService.updateFlight(this.editingOriginalId, payload);
      if (!updated) {
        this.showMessage('Unable to update flight. Flight ID may already exist.', 'error');
        return;
      }

      this.loadFlights();
      this.showMessage('Flight updated successfully.', 'success');
      this.resetForm(form);
      return;
    }

    const created = this.flightService.createFlight(payload);
    if (!created) {
      this.showMessage('Flight ID already exists. Please use a unique ID.', 'error');
      return;
    }

    this.loadFlights();
    this.showMessage('Flight created successfully.', 'success');
    this.resetForm(form);
  }

  onEdit(flight: Flight): void {
    this.isEditMode = true;
    this.submitted = false;
    this.editingOriginalId = flight.flightId;
    this.formData = { ...flight };
    this.showMessage(`Editing flight ${flight.flightId}.`, 'success');
  }

  onDelete(flightId: number): void {
    const confirmed = window.confirm(`Are you sure you want to delete flight ${flightId}?`);
    if (!confirmed) {
      return;
    }

    const deleted = this.flightService.deleteFlight(flightId);
    if (!deleted) {
      this.showMessage('Flight not found. It may have already been removed.', 'error');
      return;
    }

    this.loadFlights();

    if (this.isEditMode && this.editingOriginalId === flightId) {
      this.cancelEdit();
    }

    this.showMessage('Flight deleted successfully.', 'success');
  }

  cancelEdit(form?: NgForm): void {
    this.resetForm(form);
    this.showMessage('Edit mode cancelled.', 'success');
  }

  trackByFlightId(_: number, flight: Flight): number {
    return flight.flightId;
  }

  private loadFlights(): void {
    this.flights = this.flightService.getFlights();
  }

  private resetForm(form?: NgForm): void {
    this.formData = this.createEmptyFlight();
    this.isEditMode = false;
    this.submitted = false;
    this.editingOriginalId = null;

    if (form) {
      form.resetForm(this.createEmptyFlight());
    }
  }

  private createEmptyFlight(): Flight {
    return {
      flightId: 0,
      airline: '',
      source: '',
      destination: '',
      price: 0,
      availableSeats: 0
    };
  }

  private showMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }
}
