package com.starboard.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Parser {

    private String firstCommand;

    private String secondCommand;


    public void parse(String str){
        //strip filler words from user input
        List<String> fillerWords = Arrays.asList("to","the","a","an","from","in","inside","out","outside","of");
        String[] splitWords = str.trim().split(" ");
        List<String> command = new ArrayList<>();

        for (String word : splitWords){
            if (!fillerWords.contains(word)){
                command.add(word);
            }
        }
        //create synonyms for go command
        List<String> moveCollection = Arrays.asList("go","move","walk","run","sprint","proceed","pass");

        if (moveCollection.contains(command.get(0))){
            setFirstCommand("go");
            setSecondCommand(command.get(1));
        }
        //create synonyms for pick command
        List<String> pickItemsCollection = Arrays.asList("pick","pickup","grab","get","take","catch","capture","snag","occupy","steal","seize","grasp","snatch");

        if (pickItemsCollection.contains(command.get(0))){
            setFirstCommand("pick");
            setSecondCommand(command.get(1));
        }

        //create synonyms for drop command
        List<String> dropItemsCollection = Arrays.asList("drop","leave");
        if(dropItemsCollection.contains(command.get(0))){
            setFirstCommand("drop");
            setSecondCommand(command.get(1));
        }

        //create synonyms for use command
        List<String> useItemsCollection = Arrays.asList("use","show","display","exhibit","display","shoot","apply","fire","throw","insert","turn","push","pull","eat","utilize");

        if (useItemsCollection.contains(command.get(0))){
            setFirstCommand("use");
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