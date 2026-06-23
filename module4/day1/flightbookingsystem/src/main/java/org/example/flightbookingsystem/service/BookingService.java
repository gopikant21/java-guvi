package org.example.flightbookingsystem.service;

import org.example.flightbookingsystem.dto.BookingRequest;
import org.example.flightbookingsystem.dto.BookingResponse;
import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.exceptions.BusinessException;
import org.example.flightbookingsystem.exceptions.ResourceNotFoundException;
import org.example.flightbookingsystem.model.Booking;
import org.example.flightbookingsystem.model.BookingStatus;
import org.example.flightbookingsystem.model.Flight;
import org.example.flightbookingsystem.model.Passenger;
import org.example.flightbookingsystem.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private FlightService flightService;

    public BookingResponse create(BookingRequest request) {
        Booking booking = new Booking();
        booking.setBookingDate(Instant.now());
        booking.setBookingStatus(request.bookingStatus() == null ? BookingStatus.PENDING : request.bookingStatus());
        booking.setTotalAmount(request.totalAmount());
        booking.setPassenger(passengerService.getEntity(request.passengerId()));
        booking.setFlight(flightService.getEntity(request.flightId()));
        return toResponse(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public BookingResponse getById(Long id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public PagedResponse<BookingResponse> getAll(int page, int size, Integer passengerId) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("bookingDate").descending());
        Page<Booking> data = passengerId == null
                ? bookingRepository.findAll(pageable)
                : bookingRepository.findByPassengerPassengerId(passengerId, pageable);
        Page<BookingResponse> mapped = data.map(this::toResponse);
        return new PagedResponse<>(
                mapped.getContent(),
                mapped.getNumber(),
                mapped.getSize(),
                mapped.getTotalElements(),
                mapped.getTotalPages()
        );
    }

    public BookingResponse updateStatus(Long id, BookingStatus status) {
        Booking booking = getEntity(id);
        if (booking.getBookingStatus() == BookingStatus.CANCELLED && status == BookingStatus.CONFIRMED) {
            throw new BusinessException("Cancelled booking cannot be moved back to CONFIRMED");
        }
        booking.setBookingStatus(status);
        return toResponse(bookingRepository.save(booking));
    }

    public void delete(Long id) {
        bookingRepository.delete(getEntity(id));
    }

    Booking getEntity(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    private BookingResponse toResponse(Booking booking) {
        Passenger passenger = booking.getPassenger();
        Flight flight = booking.getFlight();
        return new BookingResponse(
                booking.getBookingId(),
                booking.getBookingDate(),
                booking.getBookingStatus(),
                booking.getTotalAmount(),
                passenger.getPassengerId(),
                passenger.getName(),
                flight.getFlightId(),
                flight.getFlightNumber()
        );
    }
}

