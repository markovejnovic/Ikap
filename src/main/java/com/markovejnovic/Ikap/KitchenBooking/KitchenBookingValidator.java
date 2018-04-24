package com.markovejnovic.Ikap.KitchenBooking;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.LocalTime;

public class KitchenBookingValidator {
    public static void validate(KitchenBooking kb) throws InvalidEmailException, InvalidDateException,
            InvalidStartTimeException, InvalidStopTimeException, InvalidPurposeException {
        validateEmail(kb);
        validateDate(kb);
        validateStartTime(kb);
        validateStopTime(kb);
        validatePurpose(kb);
    }

    public static void validateEmail(KitchenBooking kb) throws InvalidEmailException {
        if (!EmailValidator.getInstance().isValid(kb.getEmail()) ||
                !kb.getEmail().endsWith("@uwcchina.org")) {
            throw new InvalidEmailException();
        }
    }

    public static void validateDate(KitchenBooking kb) throws InvalidDateException {
        if (!kb.getDate().isAfter(LocalDate.now()) ||
                !kb.getDate().isBefore(LocalDate.now().plusDays(14))) {
            throw new InvalidDateException(
                    "Date should be after today and 14 days before today."
            );
        }
    }

    public static void validateStartTime(KitchenBooking kb) throws InvalidStartTimeException {
        if (kb.getStartTime().isBefore(LocalTime.of(9, 0)) ||
                kb.getStartTime().isAfter(LocalTime.of(21, 0))) {
            throw new InvalidStartTimeException(
                    "Invalid start time.\n" +
                            "Start Time should be after 9:00 and before 21:00"
            );
        }
    }

    public static void validateStopTime(KitchenBooking kb) throws InvalidStopTimeException {
        if (kb.getStopTime().isBefore(LocalTime.of(10, 0)) ||
                kb.getStartTime().isAfter(LocalTime.of(22, 0)) ||
                kb.getStopTime().isBefore(kb.getStartTime())) {
            throw new InvalidStopTimeException(
                    "Invalid stop time.\n" +
                            "Stop Time should be after 9:00 and before 21:00"
            );
        }
    }

    public static void validatePurpose(KitchenBooking kb) throws InvalidPurposeException {
        if (kb.getPurpose().isEmpty()) {
            throw new InvalidPurposeException();
        }
    }
}
