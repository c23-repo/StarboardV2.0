package com.starboard;

import com.starboard.util.Parser;

import java.util.Scanner;

class InputHandler {

    public static String[] input(Room room) {
        Scanner sc = new Scanner(System.in);
        Parser parser = new Parser(room);

        do {
            System.out.println("Please type in your command.\n>");
            String input = sc.nextLine();
            if (input == null) {
                //call helper doc
            } else {
                parser.parse(input);
            }
        } while (!parser.getParseStatus());

        String[] command = new String[2];
        command[0] = parser.getFirstCommand();
        command[1] = parser.getSecondCommand();

        return command;



    }
}