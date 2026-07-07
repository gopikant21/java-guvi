import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PersonDTO } from '../types/PersonDTO';

@Component({
  selector: 'app-person',
  imports: [FormsModule],
  templateUrl: './person.html',
  styleUrls: ['./person.css'],
})
export class Person {
  @Input()
  p!: PersonDTO;

  @Output()
  personRemoved = new EventEmitter<number>();

  @Output()
  personUpdated = new EventEmitter<PersonDTO>();

  isEditMode = false;
  editPerson_data: PersonDTO = { id: 0, name: '', age: 0, email: '' };

  startEdit() {
    this.isEditMode = true;
    this.editPerson_data = { ...this.p };
  }

  saveEdit() {
    this.personUpdated.emit(this.editPerson_data);
    this.isEditMode = false;
  }

  cancelEdit() {
    this.isEditMode = false;
  }

  removePerson() {
    this.personRemoved.emit(this.p.id);
  }
}
