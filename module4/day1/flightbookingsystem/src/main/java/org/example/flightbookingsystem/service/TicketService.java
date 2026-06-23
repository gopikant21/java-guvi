package org.example.flightbookingsystem.service;

import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.dto.TicketRequest;
import org.example.flightbookingsystem.dto.TicketResponse;
import org.example.flightbookingsystem.exceptions.BusinessException;
import org.example.flightbookingsystem.exceptions.ResourceNotFoundException;
import org.example.flightbookingsystem.model.Booking;
import org.example.flightbookingsystem.model.BookingStatus;
import org.example.flightbookingsystem.model.Ticket;
import org.example.flightbookingsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BookingService bookingService;

    public TicketResponse create(TicketRequest request) {
        Booking booking = bookingService.getEntity(request.bookingId());

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BusinessException("Ticket can only be generated for CONFIRMED bookings");
        }

        if (ticketRepository.existsBySeatNumberIgnoreCaseAndBookingFlightFlightId(
                request.seatNumber(), booking.getFlight().getFlightId())) {
            throw new BusinessException("Seat number already allocated on this flight: " + request.seatNumber());
        }

        Ticket ticket = new Ticket();
        ticket.setSeatNumber(request.seatNumber().trim().toUpperCase());
        ticket.setCabinClass(request.cabinClass());
        ticket.setBooking(booking);

        return toResponse(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public TicketResponse getById(Integer id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public PagedResponse<TicketResponse> getAll(int page, int size, Long bookingId) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("ticketId").descending());
        Page<Ticket> data = bookingId == null
                ? ticketRepository.findAll(pageable)
                : ticketRepository.findByBookingBookingId(bookingId, pageable);
        Page<TicketResponse> mapped = data.map(this::toResponse);

        return new PagedResponse<>(
                mapped.getContent(),
                mapped.getNumber(),
                mapped.getSize(),
                mapped.getTotalElements(),
                mapped.getTotalPages()
        );
    }

    public TicketResponse update(Integer id, TicketRequest request) {
        Ticket ticket = getEntity(id);
        Booking booking = bookingService.getEntity(request.bookingId());

        if (!ticket.getSeatNumber().equalsIgnoreCase(request.seatNumber())
                && ticketRepository.existsBySeatNumberIgnoreCaseAndBookingFlightFlightId(
                request.seatNumber(), booking.getFlight().getFlightId())) {
            throw new BusinessException("Seat number already allocated on this flight: " + request.seatNumber());
        }

        ticket.setSeatNumber(request.seatNumber().trim().toUpperCase());
        ticket.setCabinClass(request.cabinClass());
        ticket.setBooking(booking);

        return toResponse(ticketRepository.save(ticket));
    }

    public void delete(Integer id) {
        ticketRepository.delete(getEntity(id));
    }

    private Ticket getEntity(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
    }

    private TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getTicketId(),
                ticket.getSeatNumber(),
                ticket.getCabinClass(),
                ticket.getBooking().getBookingId(),
                ticket.getBooking().getFlight().getFlightId(),
                ticket.getBooking().getFlight().getFlightNumber()
        );
    }
}

