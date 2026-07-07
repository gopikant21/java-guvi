import { Component, inject } from '@angular/core';
import { CountService } from '../../services/count-service';

@Component({
  selector: 'app-decrement',
  imports: [],
  templateUrl: './decrement.html',
  styleUrl: './decrement.css',
})
export class Decrement {
  countService: CountService = inject(CountService);
}
