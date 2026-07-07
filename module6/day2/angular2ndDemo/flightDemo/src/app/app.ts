import { Component } from '@angular/core';
import { FlightDashboardComponent } from './flights/flight-dashboard.component';

@Component({
  selector: 'app-root',
  imports: [FlightDashboardComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {}
