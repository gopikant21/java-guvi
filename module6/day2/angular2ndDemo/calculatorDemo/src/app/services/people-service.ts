import { Service } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Service()
export class PeopleService {
    private names: string[] = ['Alice', 'Bob', 'Charlie', 'David', 'Eve', 'Frank', 'Grace', 'Heidi', 'Ivan', 'Judy'];
    private names$: BehaviorSubject<string[]> = new BehaviorSubject(this.names);

    getNames(): BehaviorSubject<string[]> {
        return this.names$;
    }

    addName(name: string): void {
        this.names$.next([...this.names, name]);
    }

    deleteName(name: string): void {
        this.names = this.names.filter(n => n !== name);
        this.names$.next([...this.names]);
    }

    updateName(oldName: string, newName: string): void {
        this.names = this.names.map(n => n === oldName ? newName : n);
        this.names$.next([...this.names]);
    }
}
