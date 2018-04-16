package com.markovejnovic.Ikap.KitchenBooking;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class KitchenBooking {
    private String email;
    private LocalDate date;
    private LocalTime startTime, stopTime;
    private String purpose;
    private String notes;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getDateString() {
        return this.getDate().format(DateTimeFormatter
                .ofPattern("dd/MM/yyyy"));
    }

    public void setDate(LocalDate mDate) {
        this.date = mDate;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public String getStartTimeString() {
        return this.getStartTime().format(DateTimeFormatter
                .ofPattern("HH:mm"));
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStopTime() {
        return this.stopTime;
    }

    public String getStopTimeString() {
        return this.getStopTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void setStopTime(LocalTime stopTime) {
        this.stopTime = stopTime;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public KitchenBooking() {}

    public KitchenBooking(String email, LocalDate date,
                          LocalTime startTime, LocalTime stopTime,
                          String purpose, String notes) {
        this.setEmail(email);
        this.setDate(date);
        this.setStartTime(startTime);
        this.setStopTime(stopTime);
        this.setPurpose(purpose);
        this.setNotes(notes);
    }
}
