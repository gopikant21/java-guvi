import { CommonModule } from '@angular/common';
import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app-modal.component.html',
  styleUrl: './app-modal.component.css',
})
export class AppModalComponent {
  @Input() open = false;
  @Input() title = '';

  @Output() closed = new EventEmitter<void>();

  @HostListener('document:keydown.escape')
  protected onEscape(): void {
    if (this.open) {
      this.closed.emit();
    }
  }

  protected onBackdropClick(): void {
    this.closed.emit();
  }
}
