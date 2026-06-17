package allDemo.controller;


import java.util.*;
import allDemo.entity.Flight;
import allDemo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class FlightConsoleController {

    private FlightService service;
    private final Scanner sc;

    @Autowired
    public FlightConsoleController(FlightService service, Scanner sc) {
        this.service = service;
        this.sc = sc;
    }

    public void start() {

        while (true) {
            System.out.println("\n1.Add 2.View 3.Book 4.Cancel 5.Cheapest 6.TopBooked 7.Revenue 8.Exit");
            int ch = sc.nextInt();

            switch (ch) {

                case 1 -> add();

                case 2 -> service.all().forEach(System.out::println);

                case 3 -> book();

                case 4 -> cancel();

                case 5 -> {
                    System.out.println("Source Dest:");
                    String s = sc.next();
                    String d = sc.next();
                    System.out.println(service.cheapest(s, d));
                }

                case 6 -> {
                    service.top(3).forEach(System.out::println);
                }

                case 7 -> {
                    System.out.println("Flight ID:");
                    int id = sc.nextInt();
                    System.out.println("Revenue: " + service.revenue(id));
                }

                case 8 -> System.exit(0);
            }
        }
    }

    void add() {
        Flight f = new Flight();

        System.out.println("FlightNo:");
        f.setFlightNo(sc.next());

        System.out.println("Source:");
        f.setSource(sc.next());

        System.out.println("Destination:");
        f.setDestination(sc.next());

        System.out.println("Capacity:");
        f.setCapacity(sc.nextInt());

        System.out.println("Price:");
        f.setPrice(sc.nextDouble());

        f.setBookedSeats(0);
        f.setStatus("AVAILABLE");

        service.addFlight(f);
    }

    void book() {
        System.out.println("ID seats:");
        System.out.println(service.book(sc.nextInt(), sc.nextInt()));
    }

    void cancel() {
        System.out.println("ID seats:");
        System.out.println(service.cancel(sc.nextInt(), sc.nextInt()));
    }
}