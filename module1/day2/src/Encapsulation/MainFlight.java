package Encapsulation;

public class MainFlight {
    public static void main(String[] args) {
        Flight f = new Flight("627862", "Delhi", "Chennai", 5);

        System.out.println("Flight No: " + f.getFlightNo());
        System.out.println("origin: " + f.getOrigin());
        System.out.println("destination: " + f.getDestination());
        System.out.println("rating: " + f.getRating());
    }

}
