import { Service } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Service()
export class CountService {
    private count$ = new BehaviorSubject<number>(0);

    increament(): void {
        this.count$.next(this.count$.value + 1);
    }

    decreament(): void {
        this.count$.next(this.count$.value - 1);
    }

    getCount(): Observable<number> {
        return this.count$.asObservable();
    }

    increaseBy(value: number): void {
        this.count$.next(this.count$.value + value);
    }
    
    decreaseBy(value: number): void {
        this.count$.next(this.count$.value - value);
    }
}
