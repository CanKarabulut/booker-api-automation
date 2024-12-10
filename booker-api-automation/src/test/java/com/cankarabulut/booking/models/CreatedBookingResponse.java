package com.cankarabulut.booking.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedBookingResponse {
    private Integer bookingid;
    private BookingItem booking;

}
