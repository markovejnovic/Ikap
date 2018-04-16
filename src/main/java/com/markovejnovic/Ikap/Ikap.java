package com.markovejnovic.Ikap;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.markovejnovic.Ikap.Database.DatabaseAvailabilityChecker;
import com.markovejnovic.Ikap.Database.DatabaseManager;
import com.markovejnovic.Ikap.HTTP.RequestParser;
import com.markovejnovic.Ikap.KitchenBooking.*;
import com.markovejnovic.Ikap.Supporting.Configuration;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Ikap {

    public static void main(String args[]) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(
                    Integer.parseInt(Configuration.getValues().get("PORT"))), 0);
            server.createContext("/", new IndexHandler());
            server.createContext("/request", new RequestHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            byte[] response = PageGenerator.GenerateIndex();
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) {
            Headers reqHeaders = he.getRequestHeaders();
            String contentType = reqHeaders.getFirst("Content-Type");

            if (contentType
                    .equalsIgnoreCase("application/x-www-form-urlencoded")) {
                System.out.println(contentType);

                try {
                    String request = RequestParser
                            .getWholeRequestBody(he.getRequestBody());
                    Map<String, List<String>> params = RequestParser
                            .parsePostParameters(request);

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

                            byte[] response = RequestPageGenerator
                                    .GenerateSuccess();
                            he.sendResponseHeaders(200, response.length);
                            OutputStream os = he.getResponseBody();
                            os.write(response);
                            os.close();
                        } else {
                            byte[] response = RequestPageGenerator
                                    .GenerateError(InvalidityType.DATE_TAKEN);

                            he.sendResponseHeaders(200, response.length);
                            OutputStream os = he.getResponseBody();
                            os.write(response);
                            os.close();
                        }
                    } catch (InvalidEmailException iee) {
                        byte[] response = RequestPageGenerator
                                .GenerateError(InvalidityType.EMAIL);

                        he.sendResponseHeaders(200, response.length);
                        OutputStream os = he.getResponseBody();
                        os.write(response);
                        os.close();
                    } catch (InvalidDateException ide) {
                        byte[] response = RequestPageGenerator
                                .GenerateError(InvalidityType.DATE);

                        he.sendResponseHeaders(200, response.length);
                        OutputStream os = he.getResponseBody();
                        os.write(response);
                        os.close();
                    } catch (InvalidStartTimeException iste) {
                        byte[] response = RequestPageGenerator
                                .GenerateError(InvalidityType.START_TIME);

                        he.sendResponseHeaders(200, response.length);
                        OutputStream os = he.getResponseBody();
                        os.write(response);
                        os.close();
                    } catch (InvalidStopTimeException istex) {
                        byte[] response = RequestPageGenerator
                                .GenerateError(InvalidityType.STOP_TIME);
                    } catch (InvalidPurposeException ipe) {
                        byte[] response = RequestPageGenerator
                                .GenerateError(InvalidityType.PURPOSE);

                        he.sendResponseHeaders(200, response.length);
                        OutputStream os = he.getResponseBody();
                        os.write(response);
                        os.close();
                    } catch (ClassNotFoundException cnfe) {
                        cnfe.printStackTrace();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                } catch (IOException ioe) {
                    System.err.println("Could not parse POST data. " +
                            "Sending 400 response code...");
                    try {
                        he.sendResponseHeaders(400, 0);
                        OutputStream os = he.getResponseBody();
                        os.write("".getBytes());
                        os.close();
                    } catch (IOException ioex) {
                        System.err.println("Something horrific has occured. " +
                                "Unable to send 400 response code.");
                        ioex.printStackTrace();
                    }
                }
            }
        }
    }
}
