package assignClass;

public class FuelTracker {
    double capacity;
    double currFuel;

    public FuelTracker(double capacity, double currFuel) {
        this.capacity = capacity;
        this.currFuel = currFuel;
    }

    public double getCapacity() {
        return capacity;
    }
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
    public double getCurrFuel() {
        return currFuel;
    }
    public void setCurrFuel(double currFuel) {
        this.currFuel = currFuel;
    }

    public void fillFuel(double amount) {
        this.currFuel = this.currFuel + amount;
        System.out.println("Current fuel is " + this.currFuel);
    }

    public void checkFuel() {
        System.out.println("Current fuel is " + this.currFuel);
    }

    public void drive(double km) {
        if (km > this.currFuel) {
            this.currFuel = this.currFuel - km;
        }else  {
            System.out.println("km is greater than currFuel");
        }

    }
}
