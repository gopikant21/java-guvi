package Encapsulation;

public class Flight {
    private String flightNo;
    private String origin;
    private String destination;
    private int rating;

    public Flight(String flightNo, String origin, String destination, int rating) {
        this.flightNo = flightNo;
        this.origin = origin;
        this.destination = destination;
        this.rating = rating;
    }
    public String getFlightNo() {
        return this.flightNo;
    }
    public String getOrigin() {
        return this.origin;
    }
    public String getDestination() {
        return this.destination;
    }
    public int getRating() {
        return this.rating;
    }
}
