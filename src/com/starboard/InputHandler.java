package com.starboard;

import com.starboard.util.*;

import java.util.Scanner;

class InputHandler {

    public static String[] input(Room room) {
        Scanner sc = new Scanner(System.in);
        Parser parser = new Parser(room);

        do {
            System.out.print("Please type in your command.\n> ");
            String input = sc.nextLine();
            if (input.equals("")) {
                Prompt.showCommands();
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