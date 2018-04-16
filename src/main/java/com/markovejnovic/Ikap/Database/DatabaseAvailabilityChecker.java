package com.markovejnovic.Ikap.Database;

import com.markovejnovic.Ikap.KitchenBooking.KitchenBooking;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DatabaseAvailabilityChecker {
    /**
     * Returns whether the kitchen booking date is taken in the database.
     *
     * @param kb A com.markovejnovic.Ikap.KitchenBooking object
     * @return boolean whether the kitchen booking is taken
     */
    public static boolean isDateTaken(KitchenBooking kb) {
        try {
            DatabaseManager db = new DatabaseManager();

            try {
                db.connect();
                List<LocalDate> dates = db.getDates();
                for (LocalDate date : dates) {
                    if (date.isEqual(kb.getDate())) {
                        return true;
                    }
                }
            } finally {
                db.disconnect();
            }

            return false;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return true;
    }

    /**
     * Checks whether the given com.markovejnovic.Ikap.KitchenBooking is available in the database.
     *
     * If you wish to commit a com.markovejnovic.Ikap.KitchenBooking to the database, validation
     * through this method is advised.
     *
     * @param kb A com.markovejnovic.Ikap.KitchenBooking object
     * @return boolean Whether the com.markovejnovic.Ikap.KitchenBooking is available
     */
    public static boolean isBookingAvailable(KitchenBooking kb) {
        if (!isDateTaken(kb)) {
            return true;
        } else {
            return false;
        }
    }
}
