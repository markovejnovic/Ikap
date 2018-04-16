package com.markovejnovic.Ikap;

import com.markovejnovic.Ikap.Database.DatabaseManager;
import com.markovejnovic.Ikap.KitchenBooking.KitchenBooking;
import com.markovejnovic.Ikap.Supporting.ColorGenerator;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PageGenerator {
    private static DatabaseManager db = new DatabaseManager();

    public static byte[] GenerateIndex() {
        JtwigTemplate template = JtwigTemplate
                .classpathTemplate("templates/index.twig");

        JtwigModel model = JtwigModel.newModel();
        model.with("title", "UWC CSC International Kitchen");
        model.with("emailDomain", "@uwcchina.org");

        // Get schedule
        try {
            db.connect();
            List<KitchenBooking> bookings = db.getBookingsFor2Weeks();
            db.disconnect();
            model.with("bookings", bookings);
            // TODO: Fix this hack in Twig
            List<String> backgroundColors = new ArrayList<String>();
            for (int i = 0; i < bookings.size(); i++) {
                backgroundColors.add(ColorGenerator
                        .generatePaleRandomColorHex());
            }
            model.with("bookingColors", backgroundColors);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        template.render(model, baos);
        return baos.toByteArray();
    }
}
