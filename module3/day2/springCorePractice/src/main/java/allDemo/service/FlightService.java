package allDemo.service;

import java.util.Date;
import java.util.List;
import allDemo.dao.FlightDAO;
import allDemo.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    private FlightDAO dao;

    @Autowired
    public FlightService(FlightDAO dao) {
        this.dao = dao;
    }

    public void addFlight(Flight f) {
        dao.addFlight(f);
    }

    public List<Flight> all() {
        return dao.getAllFlights();
    }

    public boolean book(int id, int seats) {
        return dao.bookSeat(id, seats);
    }

    public boolean cancel(int id, int seats) {
        return dao.cancelSeat(id, seats);
    }

    public double revenue(int id) {
        return dao.getRevenue(id);
    }

    public double occupancy(int id) {
        return dao.getOccupancyRate(id);
    }

    public Flight cheapest(String s, String d) {
        return dao.getCheapestFlight(s, d);
    }

    public List<Flight> search(String s, String d, Date date) {
        return dao.search(s, d, date);
    }

    public List<Flight> top(int n) {
        return dao.topBookedFlights(n);
    }

    public void autoUpdate() {
        dao.updateFlightStatusAutomatically();
    }
}