import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { CountService } from '../../services/count-service';
import { Increment } from "../increment/increment";
import { Decrement } from '../decrement/decrement';
import { IncreamentBy } from '../increament-by/increament-by';

@Component({
  selector: 'app-show-count',
  imports: [Increment, Decrement, IncreamentBy, AsyncPipe],
  templateUrl: './show-count.html',
  styleUrl: './show-count.css',
})
export class ShowCount {
  countService: CountService = inject(CountService);
}
