import { Component } from '@angular/core';
import { Person } from '../person/person';
import { AddPersonForm } from '../add-person-form/add-person-form';
import { PersonDTO } from '../types/PersonDTO';

@Component({
  selector: 'app-people',
  imports: [Person, AddPersonForm],
  templateUrl: './people.html',
  styleUrls: ['./people.css'],
})
export class People {
  protected people: PersonDTO[] = [
    { id: 1, name: 'John Doe', age: 30, email: 'john.doe@example.com' },
    { id: 2, name: 'Jane Smith', age: 25, email: 'jane.smith@example.com' },
    { id: 3, name: 'Alice Johnson', age: 35, email: 'alice.johnson@example.com' },
  ];

  addNewPerson(person: PersonDTO) {
    const newId = Math.max(...this.people.map(p => p.id), 0) + 1;
    const newPerson = { ...person, id: newId };
    this.people = [...this.people, newPerson];
  }

  updatePerson(updatedPerson: PersonDTO) {
    this.people = this.people.map(p => 
      p.id === updatedPerson.id ? updatedPerson : p
    );
  }

  removePerson(id: number) {
    this.people = this.people.filter(p => p.id !== id);
  }
}
