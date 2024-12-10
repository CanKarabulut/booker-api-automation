package com.cankarabulut.booking.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingItem {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private Bookingdates bookingdates;
    private String additionalneeds;
}
