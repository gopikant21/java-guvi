package allDemo.entity;

import java.util.Date;

public class Flight {

    private int id;
    private String flightNo;
    private String source;
    private String destination;

    private Date flightDate;
    private String departureTime;
    private String arrivalTime;

    private int capacity;
    private int bookedSeats;

    private double price;
    private String status; // ON_TIME / DELAYED / CANCELLED

    public Flight() {}

    public Flight(int id, String flightNo, String source, String destination,
                  Date flightDate, String departureTime, String arrivalTime,
                  int capacity, int bookedSeats, double price, String status) {
        this.id = id;
        this.flightNo = flightNo;
        this.source = source;
        this.destination = destination;
        this.flightDate = flightDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
        this.price = price;
        this.status = status;
    }

    // getters & setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFlightNo() { return flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getFlightDate() { return flightDate; }
    public void setFlightDate(Date flightDate) { this.flightDate = flightDate; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(int bookedSeats) { this.bookedSeats = bookedSeats; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return id + " | " + flightNo + " | " + source + " → " + destination +
                " | Seats: " + bookedSeats + "/" + capacity +
                " | ₹" + price + " | " + status;
    }
}