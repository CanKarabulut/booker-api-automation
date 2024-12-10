package com.cankarabulut.booking;

import com.cankarabulut.booking.models.BookingItem;
import com.cankarabulut.booking.models.CreatedBookingResponse;
import com.cankarabulut.booking.models.Bookingdates;
import com.cankarabulut.booking.models.UpdateBooking;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BookingTests extends BaseTest {

    //1. Get All Bookings
    @Test
    public void shouldGetAllBookings() {
        var response = given()
                .get(BASE_URI + "booking");
        Assertions.assertEquals(200, response.statusCode());

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");

        for (Integer bookingId: bookingIds) {
            Assertions.assertNotNull(bookingId);
        }
        System.out.println(response.prettyPrint());
    }

    //1.1 Get Bookings With FirstName
    @Test
    public void shouldGetBookingByNames() {
        String firstName = "Can";
        String lastName = "Karabulut";
        var response = given()
                .queryParam("firstname", firstName)
                .queryParam("lastName", lastName)
                .get(BASE_URI + "booking");

        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.prettyPrint());

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");

        for (Integer bookingId: bookingIds) {
            Assertions.assertNotNull(bookingId);
        }
        System.out.println(response.prettyPrint());
    }

    //2. Get Bookings By Id
    @Test
    public void shouldGetBookingById() {
        int bookingId = 1004;
        var response = given()
                .get(BASE_URI + "booking/" + bookingId);

        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.prettyPrint());

        BookingItem booking = response.as(BookingItem.class);

        Assertions.assertEquals("Josh", booking.getFirstname(), "Firstname mismatch");
        Assertions.assertEquals("Allen", booking.getLastname(), "Lastname mismatch");
        Assertions.assertEquals(111, booking.getTotalprice(), "Totalprice mismatch");
        Assertions.assertTrue(booking.isDepositpaid(), "Depositpaid should be true");
        Assertions.assertEquals("2018-01-01", booking.getBookingdates().getCheckin(), "Checkin date mismatch");
        Assertions.assertEquals("2019-01-01", booking.getBookingdates().getCheckout(), "Checkout date mismatch");
        Assertions.assertEquals("super bowls", booking.getAdditionalneeds(), "Additionalneeds mismatch");
    }

    //3. Create Booking //??
    @Test
    public void shouldCreateBooking() {
        String firstName = "Can";
        String lastName = "Karabulut";
        int totalPrice = 200;
        boolean depositpaid = true;
        String additionalneeds = "Breakfast";
        String checkin = "2020-02-02";
        String checkout = "2020-02-03";

        Bookingdates bookingDates = new Bookingdates();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        BookingItem bookingItem = new BookingItem();
        bookingItem.setFirstname(firstName);
        bookingItem.setLastname(lastName);
        bookingItem.setTotalprice(totalPrice);
        bookingItem.setDepositpaid(depositpaid);
        bookingItem.setBookingdates(bookingDates);
        bookingItem.setAdditionalneeds(additionalneeds);

        var response = given()
                .contentType(ContentType.JSON)
                .body(bookingItem)
                .post(BASE_URI + "booking");

        System.out.println(response.prettyPrint());

        Assertions.assertEquals(200, response.statusCode());

        CreatedBookingResponse bookingResponse = response.as(CreatedBookingResponse.class);
        Assertions.assertNotNull(bookingResponse.getBookingid(), "Booking ID should not be null");

        BookingItem createdBooking = bookingResponse.getBooking();
        Assertions.assertEquals(firstName, createdBooking.getFirstname(), "Firstname mismatch");
        Assertions.assertEquals("Karabulut", createdBooking.getLastname(), "Lastname mismatch");
        Assertions.assertEquals(200, createdBooking.getTotalprice(), "Totalprice mismatch");
        Assertions.assertTrue(createdBooking.isDepositpaid(), "Depositpaid should be true");


        Bookingdates createdBookingDates = createdBooking.getBookingdates();
        Assertions.assertEquals("2020-02-02", createdBookingDates.getCheckin(), "Checkin date mismatch");
        Assertions.assertEquals("2020-02-03", createdBookingDates.getCheckout(), "Checkout date mismatch");

        Assertions.assertEquals("Breakfast", createdBooking.getAdditionalneeds(), "Additionalneeds mismatch");
    }

    //4. Update Booking
    @Test
    public void shouldUpdateBooking() {
        var bookingId = 2262;
        String firstName = "Can";
        String lastName = "Karabulut";
        int totalPrice = 200;
        boolean depositpaid = true;
        String additionalneeds = "Breakfast";
        String checkin = "2020-02-02";
        String checkout = "2020-02-03";

        Bookingdates bookingdates = new Bookingdates();
        bookingdates.setCheckin(checkin);
        bookingdates.setCheckout(checkout);

        BookingItem bookingItem = new BookingItem();
        bookingItem.setFirstname(firstName);
        bookingItem.setLastname(lastName);
        bookingItem.setTotalprice(totalPrice);
        bookingItem.setDepositpaid(depositpaid);
        bookingItem.setBookingdates(bookingdates);
        bookingItem.setAdditionalneeds(additionalneeds);

        var response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .header("Cookie", "token=" + token)
                .body(bookingItem)
                .put(BASE_URI + "booking/" + bookingId);

        Assertions.assertEquals(200, response.statusCode());

        System.out.println(response.prettyPrint());

        BookingItem updatedBooking = response.as(BookingItem.class);
        Assertions.assertEquals(firstName, updatedBooking.getFirstname(), "Firstname mismatch");
        Assertions.assertEquals("Karabulut", updatedBooking.getLastname(), "Lastname mismatch");
        Assertions.assertEquals(200, updatedBooking.getTotalprice(), "Totalprice mismatch");
        Assertions.assertTrue(updatedBooking.isDepositpaid(), "Depositpaid should be true");
        Assertions.assertEquals("Breakfast", updatedBooking.getAdditionalneeds(), "Additionalneeds mismatch");

        Bookingdates bookingDates = updatedBooking.getBookingdates();
        Assertions.assertEquals("2020-02-02", bookingDates.getCheckin(), "Checkin date mismatch");
        Assertions.assertEquals("2020-02-03", bookingDates.getCheckout(), "Checkout date mismatch");
    }

    //5. Partial Update Booking
    @Test
    public void shouldPartialUpdateBooking() {
        var totalPrice = 100;
        int bookingId = 2262;

        UpdateBooking updateItem = new UpdateBooking();
        updateItem.setTotalPrice(100);

        var response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .header("Cookie", "token="+ token)
                .body(updateItem)
                .patch(BASE_URI + "booking/" + bookingId);

        Assertions.assertEquals(200, response.statusCode());
    }

    //6. Delete Booking
    @Test
    public void shouldDeleteBooking() {
        int bookingId = 1002;
        var response = given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token="+ token)
                .delete(BASE_URI + "booking/" + bookingId);

        Assertions.assertEquals(201, response.statusCode());
    }

    //HealthCheck
    @Test
    public void HealthCheck () {

        var response = given()
                .get(BASE_URI + "ping");
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("Created", response.prettyPrint());
    }
}
