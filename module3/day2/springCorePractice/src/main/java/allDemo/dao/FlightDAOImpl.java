package allDemo.dao;


import java.util.*;
import allDemo.entity.Flight;
import org.springframework.stereotype.Repository;

@Repository
public class FlightDAOImpl implements FlightDAO {

    private List<Flight> flights = new ArrayList<>();
    private int idCounter = 1;

    // ---------------- BASIC ----------------
    @Override
    public void addFlight(Flight flight) {
        flight.setId(idCounter++);
        flights.add(flight);
    }

    @Override
    public List<Flight> getAllFlights() {
        return flights;
    }

    @Override
    public Flight getFlightById(int id) {
        return flights.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateFlight(Flight flight) {
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getId() == flight.getId()) {
                flights.set(i, flight);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteFlight(int id) {
        return flights.removeIf(f -> f.getId() == id);
    }

    // ---------------- SEARCH ----------------
    @Override
    public List<Flight> search(String source, String destination, Date date) {
        List<Flight> res = new ArrayList<>();
        for (Flight f : flights) {
            if (f.getSource().equalsIgnoreCase(source)
                    && f.getDestination().equalsIgnoreCase(destination)
                    && f.getFlightDate().equals(date)) {
                res.add(f);
            }
        }
        return res;
    }

    // ---------------- SEAT BOOKING ----------------
    @Override
    public boolean bookSeat(int flightId, int seats) {
        Flight f = getFlightById(flightId);

        if (f == null) return false;

        int available = f.getCapacity() - f.getBookedSeats();

        if (seats <= 0 || seats > available) return false;

        f.setBookedSeats(f.getBookedSeats() + seats);
        updateStatus(f);
        return true;
    }

    @Override
    public boolean cancelSeat(int flightId, int seats) {
        Flight f = getFlightById(flightId);

        if (f == null || seats <= 0) return false;

        if (f.getBookedSeats() - seats < 0) return false;

        f.setBookedSeats(f.getBookedSeats() - seats);
        updateStatus(f);
        return true;
    }

    // ---------------- ANALYTICS ----------------
    @Override
    public double getRevenue(int flightId) {
        Flight f = getFlightById(flightId);
        if (f == null) return 0;

        return f.getBookedSeats() * f.getPrice();
    }

    @Override
    public double getOccupancyRate(int flightId) {
        Flight f = getFlightById(flightId);
        if (f == null) return 0;

        return (f.getBookedSeats() * 100.0) / f.getCapacity();
    }

    // ---------------- FILTER ----------------
    @Override
    public List<Flight> getFlightsByPriceRange(double min, double max) {
        List<Flight> res = new ArrayList<>();
        for (Flight f : flights) {
            if (f.getPrice() >= min && f.getPrice() <= max)
                res.add(f);
        }
        return res;
    }

    @Override
    public List<Flight> getFlightsByDateRange(Date start, Date end) {
        List<Flight> res = new ArrayList<>();
        for (Flight f : flights) {
            if (!f.getFlightDate().before(start) && !f.getFlightDate().after(end))
                res.add(f);
        }
        return res;
    }

    // ---------------- STATUS FILTER ----------------
    @Override
    public List<Flight> getAvailableFlights() {
        List<Flight> res = new ArrayList<>();
        for (Flight f : flights)
            if (f.getBookedSeats() < f.getCapacity())
                res.add(f);
        return res;
    }

    @Override
    public List<Flight> getFullFlights() {
        List<Flight> res = new ArrayList<>();
        for (Flight f : flights)
            if (f.getBookedSeats() >= f.getCapacity())
                res.add(f);
        return res;
    }

    @Override
    public List<Flight> getDelayedFlights() {
        List<Flight> res = new ArrayList<>();
        for (Flight f : flights)
            if ("DELAYED".equalsIgnoreCase(f.getStatus()))
                res.add(f);
        return res;
    }

    // ---------------- SORTING ----------------
    @Override
    public List<Flight> sortByPrice() {
        flights.sort(Comparator.comparingDouble(Flight::getPrice));
        return flights;
    }

    @Override
    public List<Flight> sortBySeats() {
        flights.sort(Comparator.comparingInt(Flight::getBookedSeats));
        return flights;
    }

    @Override
    public List<Flight> sortByDate() {
        flights.sort(Comparator.comparing(Flight::getFlightDate));
        return flights;
    }

    // ---------------- ADVANCED ----------------
    @Override
    public Flight getCheapestFlight(String source, String destination) {
        return flights.stream()
                .filter(f -> f.getSource().equalsIgnoreCase(source)
                        && f.getDestination().equalsIgnoreCase(destination))
                .min(Comparator.comparingDouble(Flight::getPrice))
                .orElse(null);
    }

    @Override
    public List<Flight> topBookedFlights(int limit) {
        return flights.stream()
                .sorted((a, b) -> b.getBookedSeats() - a.getBookedSeats())
                .limit(limit)
                .toList();
    }

    // ---------------- AUTO STATUS ----------------
    @Override
    public void updateFlightStatusAutomatically() {
        for (Flight f : flights) {
            updateStatus(f);
        }
    }

    private void updateStatus(Flight f) {
        if (f.getBookedSeats() >= f.getCapacity()) {
            f.setStatus("FULL");
        } else {
            f.setStatus("AVAILABLE");
        }
    }
}