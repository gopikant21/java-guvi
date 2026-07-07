import { Injectable } from '@angular/core';
import { Flight } from './flight.model';

@Injectable({
  providedIn: 'root'
})
export class FlightService {
  private flights: Flight[] = [
    {
      flightId: 1001,
      airline: 'SkyJet Airways',
      source: 'Delhi',
      destination: 'Mumbai',
      price: 5400,
      availableSeats: 24
    },
    {
      flightId: 1002,
      airline: 'AeroVista',
      source: 'Bengaluru',
      destination: 'Chennai',
      price: 3600,
      availableSeats: 16
    },
    {
      flightId: 1003,
      airline: 'Nimbus Air',
      source: 'Hyderabad',
      destination: 'Kolkata',
      price: 6200,
      availableSeats: 31
    }
  ];

  getFlights(): Flight[] {
    return this.flights.map((flight) => ({ ...flight }));
  }

  createFlight(flight: Flight): boolean {
    if (this.flights.some((item) => item.flightId === flight.flightId)) {
      return false;
    }

    this.flights.unshift({ ...flight });
    return true;
  }

  updateFlight(originalFlightId: number, updatedFlight: Flight): boolean {
    const index = this.flights.findIndex((flight) => flight.flightId === originalFlightId);
    if (index === -1) {
      return false;
    }

    const duplicateId = this.flights.some(
      (flight, idx) => idx !== index && flight.flightId === updatedFlight.flightId
    );

    if (duplicateId) {
      return false;
    }

    this.flights[index] = { ...updatedFlight };
    return true;
  }

  deleteFlight(flightId: number): boolean {
    const index = this.flights.findIndex((flight) => flight.flightId === flightId);
    if (index === -1) {
      return false;
    }

    this.flights.splice(index, 1);
    return true;
  }
}
