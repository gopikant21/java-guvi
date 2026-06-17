package allDemo.dao;

import java.util.Date;
import java.util.List;
import allDemo.entity.Flight;

public interface FlightDAO {

    void addFlight(Flight flight);

    List<Flight> getAllFlights();

    Flight getFlightById(int id);

    boolean updateFlight(Flight flight);

    boolean deleteFlight(int id);

    //SEARCH
    List<Flight> search(String source, String destination, Date date);

    //SEAT OPS
    boolean bookSeat(int flightId, int seats);
    boolean cancelSeat(int flightId, int seats);

    //ANALYTICS
    double getRevenue(int flightId);
    double getOccupancyRate(int flightId);

    //FILTERING
    List<Flight> getFlightsByPriceRange(double min, double max);
    List<Flight> getFlightsByDateRange(Date start, Date end);

    //BUSINESS LOGIC
    List<Flight> getAvailableFlights();
    List<Flight> getFullFlights();
    List<Flight> getDelayedFlights();

    //SORTING
    List<Flight> sortByPrice();
    List<Flight> sortBySeats();
    List<Flight> sortByDate();

    //ADVANCED
    Flight getCheapestFlight(String source, String destination);
    List<Flight> topBookedFlights(int limit);

    void updateFlightStatusAutomatically();
}