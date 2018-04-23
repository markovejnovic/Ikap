package com.markovejnovic.Ikap;

import com.markovejnovic.Ikap.HTTP.RequestParser;
import com.markovejnovic.Ikap.Request.ResponseGenerator;
import com.markovejnovic.Ikap.Supporting.Configuration;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.parboiled.common.Tuple2;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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

                try {
                    String request = RequestParser
                            .getWholeRequestBody(he.getRequestBody());
                    Tuple2<String, Integer> responsePair = ResponseGenerator.generateResponse(RequestParser
                            .parsePostParameters(request));

                    he.sendResponseHeaders(responsePair.b, responsePair.a.getBytes().length);
                    OutputStream os = he.getResponseBody();
                    os.write(responsePair.a.getBytes());
                    os.close();
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
