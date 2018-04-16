package com.markovejnovic.Ikap.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {
    private static String DEFAULT_ENCODING = "ISO-8859-1";
    public static String getWholeRequestBody(InputStream requestBody)
            throws IOException {
        String request;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[4096];
            for (int n = requestBody.read(buf); n > 0;
                 n = requestBody.read(buf)) {
                out.write(buf, 0, n);
            }
            request = new String(out.toByteArray(), DEFAULT_ENCODING);
        } finally {
            requestBody.close();
        }
        return request;
    }

    public static String getWholeRequestBody(
            InputStream requestBody, String encoding) throws IOException {
        String request;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[4096];
            for (int n = requestBody.read(buf); n > 0;
                 n = requestBody.read(buf)) {
                out.write(buf, 0, n);
            }
            request = new String(out.toByteArray(), encoding);
        } finally {
            requestBody.close();
        }
        return request;
    }

    public static Map<String, List<String>> parsePostParameters(String request)
            throws UnsupportedEncodingException {
        Map<String, List<String>> params = new HashMap<String,List<String>>();
        String defs[] = request.split("[&]");
        for (String def : defs) {
            int ix = def.indexOf('=');
            String name;
            String value;
            if (ix < 0) {
                name = URLDecoder.decode(def, DEFAULT_ENCODING);
                value = "";
            } else {
                name = URLDecoder.decode(def.substring(0, ix),
                        DEFAULT_ENCODING);
                value = URLDecoder.decode(def.substring(ix + 1),
                        DEFAULT_ENCODING);
            }
            List<String> list = params.computeIfAbsent(name,
                    k -> new ArrayList<String>());
            list.add(value);
        }
        return params;
    }

    public static Map<String, List<String>> parsePostParameters(
            String request, String encoding)
            throws UnsupportedEncodingException {
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        String defs[] = request.split("[&]");
        for (String def : defs) {
            int ix = def.indexOf('=');
            String name;
            String value;
            if (ix < 0) {
                name = URLDecoder.decode(def, encoding);
                value = "";
            } else {
                name = URLDecoder.decode(def.substring(0, ix),
                        encoding);
                value = URLDecoder.decode(def.substring(ix + 1),
                        encoding);
            }
            List<String> list = params.computeIfAbsent(name,
                    k -> new ArrayList<String>());
            list.add(value);
        }
        return params;
    }
}
