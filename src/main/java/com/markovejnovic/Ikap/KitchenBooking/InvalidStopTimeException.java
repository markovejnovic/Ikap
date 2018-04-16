package com.markovejnovic.Ikap.KitchenBooking;

import java.security.InvalidParameterException;

public class InvalidStopTimeException extends InvalidParameterException {
    InvalidStopTimeException() {}

    InvalidStopTimeException(String msg) {
        super(msg);
    }
}
