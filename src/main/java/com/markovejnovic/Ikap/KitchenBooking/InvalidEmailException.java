package com.markovejnovic.Ikap.KitchenBooking;

import java.security.InvalidParameterException;

public class InvalidEmailException extends InvalidParameterException {
    InvalidEmailException() {}

    InvalidEmailException(String msg) {
        super(msg);
    }
}
