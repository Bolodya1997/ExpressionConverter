package ru.nsu.fit.g14203.popov;

import ru.nsu.fit.g14203.popov.logic.LogicSimplifier;
import ru.nsu.fit.g14203.popov.parse.ParseException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LogicSimplifier logicSimplifier = new LogicSimplifier();

        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String string = input.nextLine();
            if (string.isEmpty())
                continue;

            if (string.equals("quit"))
                return;

            try {
                System.out.println(logicSimplifier.simplify(string));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
