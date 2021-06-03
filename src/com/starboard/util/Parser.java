package com.starboard.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Parser {

    private String firstCommand;

    private String secondCommand;


    public void parse(String str){

        List<String> fillerWords = Arrays.asList("to","the","a","an","from","in","inside","out","outside","of");
        String[] splitWords = str.trim().split(" ");
        List<String> command = new ArrayList<>();

        for (String word : splitWords){
            if (!fillerWords.contains(word)){
                command.add(word);
            }
        }

        List<String> moveCollection = Arrays.asList("go","move","walk","run","sprint","proceed","pass");

        if (moveCollection.contains(command.get(0))){
            setFirstCommand("go");
            setSecondCommand(command.get(1));
        }

        List<String> pickItemsCollection = Arrays.asList("pick","pickup","grab","get","take","catch","capture","snag","occupy","steal","seize","grasp","snatch");

        if (pickItemsCollection.contains(command.get(0))){
            setFirstCommand("pick");
            setSecondCommand(command.get(1));
        }
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