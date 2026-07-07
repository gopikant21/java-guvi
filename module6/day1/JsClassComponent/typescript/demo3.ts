interface SaysHi {
    sayhi(): void;
}

class Person implements SaysHi {
    constructor(private fname: string, private lname: string) {}

    fullName(): string {
        return this.fname + " " + this.lname;
    }

    disp(): void {
        console.log("hi " + this.fullName());
    }

    sayhi(): void {
        console.log("hi all");
    }
}

const p1 = new Person("Sachin", "Tendulkar");
p1.disp();
p1.sayhi();
