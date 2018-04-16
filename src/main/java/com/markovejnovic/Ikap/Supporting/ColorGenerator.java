package com.markovejnovic.Ikap.Supporting;

import java.util.Random;

public class ColorGenerator {
    public static String generatePaleRandomColorHex() {
        Random rnd = new Random();

        int r = (int) ((rnd.nextFloat() / 2f + 0.5f) * 255);
        int g = (int) ((rnd.nextFloat() / 2f + 0.5f) * 255);
        int b = (int) ((rnd.nextFloat() / 2f + 0.5f) * 255);

        return String.format("#%02x%02x%02x", r, g, b);
    }
}