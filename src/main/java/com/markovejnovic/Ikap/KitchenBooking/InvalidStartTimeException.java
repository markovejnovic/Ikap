package com.markovejnovic.Ikap.KitchenBooking;

import java.security.InvalidParameterException;

public class InvalidStartTimeException extends InvalidParameterException {
    InvalidStartTimeException() {}

    InvalidStartTimeException(String msg) {
        super(msg);
    }
}
