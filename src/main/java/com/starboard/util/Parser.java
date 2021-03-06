package com.starboard.util;

import com.starboard.Game;
import com.starboard.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    static final List<String> MOVE_COLLECTION = Arrays.asList("go", "move", "walk", "run", "sprint", "proceed", "pass", "enter");
    static final List<String> PICK_ITEMS_COLLECTION = Arrays.asList("pick", "pickup", "grab", "get", "take", "catch", "capture", "snag", "occupy", "steal", "seize", "grasp", "snatch");
    static final List<String> DROP_ITEMS_COLLECTION = Arrays.asList("drop", "leave", "discard");
    static final List<String> USE_ITEMS_COLLECTION = Arrays.asList("use", "kill", "show", "display", "exhibit", "display", "shoot", "apply", "fire", "throw", "insert", "turn", "push", "pull", "eat", "utilize");
    static final List<String> OPEN_CONTAINERS_COLLECTION = Arrays.asList("open", "look", "see", "discover", "watch");
    static final List<String> SINGLE_ENTRY_ROOM_NAMES = Arrays.asList("bridge", "lab", "enginebay");
    static final List<String> EXIT_COLLECTION = Arrays.asList("exit", "leave");
    private final Room room;
    private String firstCommand;
    private String secondCommand;
    private boolean parseStatus;

    public Parser(Room room) {
        this.room = room;
    }

    public static String aOrAn(String itemName) {
        String article;
        if (Arrays.asList('a', 'e', 'i', 'o', 'u').contains(itemName.charAt(0))) {
            article = "an";
        } else {
            article = "a";
        }
        return article;
    }

    public void parse(String str) {
        //strip filler words from user input
        List<String> fillerWords = Arrays.asList("to", "the", "a", "an", "from", "in", "inside", "out", "outside", "of", "me", "at");
        String[] splitWords = str.trim().split("\\W"); // removing all non-Alpha characters
        List<String> command = new ArrayList<>();

        //build the command
        for (String word : splitWords) {
            if (!fillerWords.contains(word)) {
                command.add(word);
            }
        }

        if (command.size() == 0) {
            setParseStatus(false);

        } else if (command.size() == 1) {
            //if currentroom is bridge||lab||enginebay, can use exit and leave
            setFirstCommand("exit");
            Room nextRoom = room.getPaths().get(room.getLinkedRooms().get(0));
            setSecondCommand(nextRoom.getName());
            if (SINGLE_ENTRY_ROOM_NAMES.contains(room.getName()) && EXIT_COLLECTION.contains(command.get(0))) {
                setParseStatus(true);
            } else if (EXIT_COLLECTION.contains(command.get(0)) && !SINGLE_ENTRY_ROOM_NAMES.contains(room.getName())) {
                setParseStatus(false);
                System.out.println("You cannot use exit on multi-door room, please use go command.");
            } else if (command.get(0).equalsIgnoreCase("sound")) {
                setFirstCommand("sound");
                setParseStatus(true);
            } else if (command.get(0).equalsIgnoreCase("help")) {
                setFirstCommand("help");
                setParseStatus(true);
            } else if (command.get(0).equalsIgnoreCase("quit")) {
                setFirstCommand("quit");
                setParseStatus(true);
            } else {
                System.out.println("Unrecognized command");

            }
        } else {

            //create synonyms for go command
            if (MOVE_COLLECTION.contains(command.get(0).toLowerCase())) {
                setFirstCommand("go");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }
            //create synonyms for pick command
            if (PICK_ITEMS_COLLECTION.contains(command.get(0).toLowerCase())) {
                setFirstCommand("get");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }

            //create synonyms for drop command
            if (DROP_ITEMS_COLLECTION.contains(command.get(0).toLowerCase())) {
                setFirstCommand("drop");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }

            //create synonyms for use command
            if (Game.getGameMusic() == Music.battleMusic) { // allow only "use" during the battle
                if (command.get(0).equalsIgnoreCase("use")) {
                    setFirstCommand("use");
                    setSecondCommand(command.get(1).toLowerCase());
                    setParseStatus(true);
                }
            } else if (USE_ITEMS_COLLECTION.contains(command.get(0).toLowerCase())) {
                setFirstCommand("use");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }

            // create synonyms for open command
            if (OPEN_CONTAINERS_COLLECTION.contains(command.get(0).toLowerCase())) {
                setFirstCommand("open");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }
        }
    }

    public boolean getParseStatus() {
        return parseStatus;
    }

    public void setParseStatus(boolean parseStatus) {
        this.parseStatus = parseStatus;
    }

    public String getFirstCommand() {
        return firstCommand;
    }

    public void setFirstCommand(String firstCommand) {
        this.firstCommand = firstCommand;
    }

    public String getSecondCommand() {
        return secondCommand;
    }

    public void setSecondCommand(String secondCommand) {
        this.secondCommand = secondCommand;
    }
}