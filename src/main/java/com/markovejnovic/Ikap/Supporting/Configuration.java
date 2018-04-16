package com.markovejnovic.Ikap.Supporting;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + ".config/Ikap/Ikap.conf";

    private static final Map<String, String> DEFAULT_CONFIGURATION;
    private static Map<String, String> configValues;

    static {
        DEFAULT_CONFIGURATION = new HashMap<String, String>();
        DEFAULT_CONFIGURATION.put("DB_PATH", System.getProperty("user.home") + File.separator + "ikap.db");
        DEFAULT_CONFIGURATION.put("PORT", "5469");

        configValues = getConfiguration();
    }

    private static Map<String, String> getConfiguration() {
        Map<String, String> configurationValues = DEFAULT_CONFIGURATION;

        File configFile = new File(CONFIG_PATH);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(configFile));
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] lineSeparated = line.replaceAll("\\s+", "").split("=");
                String key = lineSeparated[0];
                String value = lineSeparated[1].replaceAll("\"", "")
                        .replaceFirst("^~",System.getProperty("user.home"));
                configurationValues.computeIfPresent(key, (k, v) -> value);
            }
        } catch (IOException ioe) {
            System.out.println("Unable to find configuration file. Will resort to default values.");
            configurationValues = DEFAULT_CONFIGURATION;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioe) {
                System.out.println("Unable to close BufferedReader.");
                ioe.printStackTrace();
            }
        }

        return configurationValues;
    }

    public static String getDBPath() {
        return configValues.get("DB_PATH");
    }

    public static Map<String, String> getValues() {
        return configValues;
    }
}
