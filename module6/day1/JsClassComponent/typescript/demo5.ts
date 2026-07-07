interface Printable {
    printSummary(): void;
}

class Person {
    constructor(public firstName: string, public lastName: string) {}

    get fullName(): string {
        return this.firstName + " " + this.lastName;
    }
}

class Loan implements Printable {
    private static nextId = 1;
    public readonly id: number;
    private _interestRate: number;

    constructor(
        public borrower: Person,
        public principal: number,
        interestRate: number,
        public tenureMonths: number
    ) {
        this.id = Loan.nextId++;
        this.interestRate = interestRate;
    }

    get interestRate(): number {
        return this._interestRate;
    }

    set interestRate(value: number) {
        if (value <= 0 || value > 50) {
            throw new Error("Interest rate must be between 0 and 50");
        }
        this._interestRate = value;
    }

    calculateSimpleInterest(): number {
        return (this.principal * this.interestRate * this.tenureMonths) / (100 * 12);
    }

    calculateTotalPayable(): number {
        return this.principal + this.calculateSimpleInterest();
    }

    printSummary(): void {
        console.log(
            "Loan#" + this.id +
                " | Borrower: " + this.borrower.fullName +
                " | Principal: " + this.principal +
                " | Rate: " + this.interestRate + "%" +
                " | Total: " + this.calculateTotalPayable().toFixed(2)
        );
    }
}

class HomeLoan extends Loan {
    constructor(
        borrower: Person,
        principal: number,
        interestRate: number,
        tenureMonths: number,
        public propertyValue: number
    ) {
        super(borrower, principal, interestRate, tenureMonths);
    }

    calculateLtv(): number {
        return (this.principal / this.propertyValue) * 100;
    }

    printSummary(): void {
        super.printSummary();
        console.log("LTV: " + this.calculateLtv().toFixed(2) + "%");
    }
}

class LoanRepository<T extends Loan> {
    private items: T[] = [];

    add(loan: T): void {
        this.items.push(loan);
    }

    findById(id: number): T | undefined {
        for (let i = 0; i < this.items.length; i++) {
            if (this.items[i].id === id) {
                return this.items[i];
            }
        }
        return undefined;
    }

    getAll(): readonly T[] {
        return this.items;
    }
}

const borrower1 = new Person("Sachin", "Tendulkar");
const borrower2 = new Person("Virat", "Kohli");

const personalLoan = new Loan(borrower1, 500000, 11.5, 24);
const homeLoan = new HomeLoan(borrower2, 2500000, 8.1, 180, 4000000);

const loanRepo = new LoanRepository<Loan>();
loanRepo.add(personalLoan);
loanRepo.add(homeLoan);

loanRepo.getAll().forEach((loan) => loan.printSummary());

const selectedLoan = loanRepo.findById(2);
if (selectedLoan) {
    console.log("Found loan for: " + selectedLoan.borrower.fullName);
}