package ru.nsu.fit.g14203.popov;

import ru.nsu.fit.g14203.popov.converter.Converter;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class Main {

    public static void main(String[] args) {
        Reader input = new InputStreamReader(System.in);
        Writer output = new OutputStreamWriter(System.out);
        try {
            new Converter(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
