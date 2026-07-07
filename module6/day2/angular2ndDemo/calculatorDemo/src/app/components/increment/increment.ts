import { Component, inject } from '@angular/core';
import { CountService } from '../../services/count-service';

@Component({
  selector: 'app-increment',
  imports: [],
  templateUrl: './increment.html',
  styleUrl: './increment.css',
})
export class Increment {
  countService: CountService = inject(CountService);
}
