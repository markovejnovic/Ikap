package com.markovejnovic.Ikap.KitchenBooking;

import java.security.InvalidParameterException;

public class InvalidPurposeException extends InvalidParameterException {
    InvalidPurposeException() {}

    InvalidPurposeException(String msg) {
        super(msg);
    }
}
