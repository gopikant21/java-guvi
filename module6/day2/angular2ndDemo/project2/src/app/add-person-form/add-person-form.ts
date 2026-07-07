import { Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PersonDTO } from '../types/PersonDTO';

@Component({
  selector: 'app-add-person-form',
  imports: [FormsModule],
  templateUrl: './add-person-form.html',
  styleUrls: ['./add-person-form.css'],
})
export class AddPersonForm {
  newPerson: PersonDTO = { id: 0, name: '', age: 0, email: '' };

  @Output()
  personAdded = new EventEmitter<PersonDTO>();

  addPerson() {
    if (this.newPerson.name && this.newPerson.age && this.newPerson.email) {
      this.personAdded.emit({...this.newPerson});
      this.newPerson = { id: 0, name: '', age: 0, email: '' };
    }
  }
}
