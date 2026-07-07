import { Component, inject, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CountService } from '../../services/count-service';

@Component({
  selector: 'app-increament-by',
  imports: [FormsModule],
  templateUrl: './increament-by.html',
  styleUrl: './increament-by.css',
})
export class IncreamentBy {
  countService: CountService = inject(CountService);

  @Input() incrementValue: number = 0;
}
