package com.starboard;

import com.starboard.util.*;

import java.io.Console;
import java.util.Scanner;

public class InputHandler {

    public static String[] input(Room room) {
        //check scanner from file
        Scanner sc = new Scanner(System.in);
        Parser parser = new Parser(room);

        do {
            ConsoleColors.changeTo(ConsoleColors.BLACK_BOLD);
            System.out.print(ConsoleColors.WHITE_BACKGROUND  + "Please type in your command.   Enter " + ConsoleColors.GREEN_BACKGROUND+"\"help\"" +
                    ConsoleColors.WHITE_BACKGROUND + " for help with the commands,  " + ConsoleColors.GREEN_BACKGROUND + "\"quit\"" +
                    ConsoleColors.WHITE_BACKGROUND + " to quit the game or the training\n> "+ ConsoleColors.RESET);
            ConsoleColors.reset();
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

    public static String getUserInput(String displayMessage) {
        System.out.print(displayMessage + "\n>");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}