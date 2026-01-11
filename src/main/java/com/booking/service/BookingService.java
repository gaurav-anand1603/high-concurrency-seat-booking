package com.booking.service;

import com.booking.model.Booking;
import com.booking.model.Seat;
import com.booking.model.SeatStatus;
import com.booking.repository.BookingRepository;
import com.booking.repository.SeatRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final LockService lockService;

    @Transactional
    public Booking bookSeat(Long seatId, Long userId){
        String lockKey = "lock:seat:" + seatId;
        String lockValue = lockService.lockSeat(lockKey);
        if (lockValue == null) {
            throw new RuntimeException("Seat is currently being booked by another user");
        }
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat Id is not found"));
            if(seat.getSeatStatus() == SeatStatus.BOOKED){
                throw new RuntimeException("Seat already booked");
            }
            // Seat is available
            seat.setSeatStatus(SeatStatus.BOOKED);
            seatRepository.save(seat);

            Booking booking = new Booking();
            booking.setSeatId(seatId);
            booking.setUserId(userId);
            booking.setCreatedAt(LocalDateTime.now());
            return bookingRepository.save(booking);

        } finally {
            lockService.releaseLock(lockKey, lockValue);
        }

    }


}
