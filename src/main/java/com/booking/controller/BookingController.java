package com.booking.controller;


import com.booking.model.Booking;
import com.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking bookSeat(@RequestParam Long seatId, @RequestParam Long userId){
        return bookingService.bookSeat(seatId, userId);
    }
}
