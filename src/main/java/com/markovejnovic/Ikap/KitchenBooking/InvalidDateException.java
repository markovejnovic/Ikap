package com.markovejnovic.Ikap.KitchenBooking;

import java.security.InvalidParameterException;

public class InvalidDateException extends InvalidParameterException {
    InvalidDateException() {}

    InvalidDateException(String msg) {
        super(msg);
    }
}
