package org.example.flightbookingsystem.service;

import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.dto.PaymentRequest;
import org.example.flightbookingsystem.dto.PaymentResponse;
import org.example.flightbookingsystem.exceptions.BusinessException;
import org.example.flightbookingsystem.exceptions.ResourceNotFoundException;
import org.example.flightbookingsystem.model.Booking;
import org.example.flightbookingsystem.model.BookingStatus;
import org.example.flightbookingsystem.model.Payment;
import org.example.flightbookingsystem.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingService bookingService;

    public PaymentResponse create(PaymentRequest request) {
        if (paymentRepository.existsByBookingBookingId(request.bookingId())) {
            throw new BusinessException("Payment already exists for booking id: " + request.bookingId());
        }

        Booking booking = bookingService.getEntity(request.bookingId());
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Cannot add payment for a cancelled booking");
        }

        Payment payment = new Payment();
        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setMode(request.mode());
        payment.setBooking(booking);

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public PaymentResponse getById(Integer id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public PaymentResponse getByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PaymentResponse> getAll(int page, int size) {
        Page<PaymentResponse> mapped = paymentRepository
                .findAll(PageRequest.of(page, size, Sort.by("paymentId").descending()))
                .map(this::toResponse);
        return new PagedResponse<>(
                mapped.getContent(),
                mapped.getNumber(),
                mapped.getSize(),
                mapped.getTotalElements(),
                mapped.getTotalPages()
        );
    }

    public PaymentResponse update(Integer id, PaymentRequest request) {
        Payment payment = getEntity(id);

        if (!payment.getBooking().getBookingId().equals(request.bookingId())
                && paymentRepository.existsByBookingBookingId(request.bookingId())) {
            throw new BusinessException("Another payment already exists for booking id: " + request.bookingId());
        }

        Booking booking = bookingService.getEntity(request.bookingId());
        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setMode(request.mode());
        payment.setBooking(booking);

        return toResponse(paymentRepository.save(payment));
    }

    public void delete(Integer id) {
        paymentRepository.delete(getEntity(id));
    }

    private Payment getEntity(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getMode(),
                payment.getBooking().getBookingId()
        );
    }
}

