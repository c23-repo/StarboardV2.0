package com.starboard.util;

import com.starboard.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private String firstCommand;

    private String secondCommand;

    private boolean parseStatus;

    private final Room room;

    public Parser(Room room){
        this.room = room;
    }


    public void parse(String str) {
        //strip filler words from user input
        List<String> fillerWords = Arrays.asList("to", "the", "a", "an", "from", "in", "inside", "out", "outside", "of");
        String[] splitWords = str.trim().split(" ");
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
            List<String> singleEntryRoomNames = Arrays.asList("bridge","lab","enginebay");
            List<String> exitCollection = Arrays.asList("exit","leave");
            //if currentroom is bridge||lab||enginebay, can use exit and leave
            setFirstCommand("exit");
            Room nextRoom = room.getPaths().get(room.getLinkedRooms().get(0));
            setSecondCommand(nextRoom.getName());
            if(singleEntryRoomNames.contains(room.getName()) && exitCollection.contains(command.get(0))){
                setParseStatus(true);
            }else{
                setParseStatus(false);
                System.out.println("You cannot use exit on multi-door room, please use go command.");
            }
        } else {

            //create synonyms for go command
            List<String> moveCollection = Arrays.asList("go", "move", "walk", "run", "sprint", "proceed", "pass");

            if (moveCollection.contains(command.get(0).toLowerCase())) {
                setFirstCommand("go");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }
            //create synonyms for pick command
            List<String> pickItemsCollection = Arrays.asList("pick", "pickup", "grab", "get", "take", "catch", "capture", "snag", "occupy", "steal", "seize", "grasp", "snatch");

            if (pickItemsCollection.contains(command.get(0).toLowerCase())) {
                setFirstCommand("pick");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }

            //create synonyms for drop command
            List<String> dropItemsCollection = Arrays.asList("drop", "leave", "discard");
            if (dropItemsCollection.contains(command.get(0).toLowerCase())) {
                setFirstCommand("drop");
                setSecondCommand(command.get(1).toLowerCase());
                setParseStatus(true);
            }

            //create synonyms for use command
            List<String> useItemsCollection = Arrays.asList("use", "kill","show", "display", "exhibit", "display", "shoot", "apply", "fire", "throw", "insert", "turn", "push", "pull", "eat", "utilize");

            if (useItemsCollection.contains(command.get(0).toLowerCase())) {
                setFirstCommand("use");
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