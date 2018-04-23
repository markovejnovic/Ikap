package com.markovejnovic.Ikap.Request;

import com.markovejnovic.Ikap.Database.DatabaseAvailabilityChecker;
import com.markovejnovic.Ikap.Database.DatabaseManager;
import com.markovejnovic.Ikap.KitchenBooking.*;
import org.parboiled.common.Tuple2;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ResponseGenerator {
    public static Tuple2<String, Integer> generateResponse(Map<String, List<String>> params) {
        try {
            KitchenBooking kb = new KitchenBooking();
            kb.setEmail(params.get("request-email").get(0));
            kb.setDate(LocalDate.parse(
                    params.get("request-date").get(0),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            kb.setStartTime(LocalTime.parse(
                    params.get("request-startTime").get(0),
                    DateTimeFormatter.ofPattern("HH:mm")));
            kb.setStopTime(LocalTime.parse(
                    params.get("request-stopTime").get(0),
                    DateTimeFormatter.ofPattern("HH:mm")));
            kb.setPurpose(params.get("request-purpose").get(0));
            kb.setNotes(params.get("request-notes").get(0));

            KitchenBookingValidator.validate(kb);

            if (DatabaseAvailabilityChecker.isBookingAvailable(kb)) {
                DatabaseManager db = new DatabaseManager();
                db.connect();
                db.addBooking(kb);
                db.disconnect();

                return new Tuple2<String, Integer>(Responses.BOOKING_SUCCESS.name(), 200);
            } else {
                return new Tuple2<String, Integer>(Responses.DATE_TAKEN.name(), 400);
            }
        } catch (InvalidEmailException iee) {
            return new Tuple2<String, Integer>(Responses.INVALID_EMAIL.name(), 400);
        } catch (InvalidDateException ide) {
            return new Tuple2<String, Integer>(Responses.INVALID_DATE.name(), 400);
        } catch (InvalidStartTimeException iste) {
            return new Tuple2<String, Integer>(Responses.INVALID_START_TIME.name(), 400);
        } catch (InvalidStopTimeException istex) {
            return new Tuple2<String, Integer>(Responses.INVALID_STOP_TIME.name(), 400);
        } catch (InvalidPurposeException ipe) {
            return new Tuple2<String, Integer>(Responses.INVALID_PURPOSE.name(), 400);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return new Tuple2<String, Integer>(Responses.INTERNAL_ERROR.name(), 500);
    }
}
