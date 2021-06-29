package com.starboard.items;

import java.util.HashMap;
import java.util.Map;

import static com.starboard.util.Parser.aOrAn;

public class Container extends GameItem {
    //fields

    private final Map<String, GameItem> contents = new HashMap<>();
    // Contents of the container are hidden by default if not specified in constructor.
    private boolean contentsHidden;

    // Constructors

    public Container(String name) {
        super(name);
        contentsHidden = true;
        isPortable = false;
    }

    public Container(String name, String description) {
        this(name);
        setDescription(description);
    }

    public Container(String name, boolean contentsHidden) {
        super(name);
        this.contentsHidden = contentsHidden;
        isPortable = false;
    }

    public Container(String name, String description, boolean contentsHidden) {
        super(name, description);
        this.contentsHidden = contentsHidden;
        isPortable = false;
    }

    // Business

    public GameItem giveItem(String name) throws NullPointerException {
        GameItem item = getContentItem(name);
        contents.remove(name);
        return item;
    }

    public void addItem(GameItem item) {
        try {
            getContentItem(item.getName()).changeQuantity(item.getQuantity());
        } catch (NullPointerException e) {
            contents.put(item.getName(), item);
        }
    }

    public void open() {
        if (contentsHidden) {
            contentsHidden = false;
            System.out.println("You see:");
            for (GameItem item : contents.values()) {
                if (item.getQuantity() > 1) {
                    System.out.printf("%d %ss%n", item.getQuantity(), item.getName());
                } else {
                    System.out.printf("%s %s%n", aOrAn(item.getName()), item.getName());
                }
            }
        } else {
            System.out.println("You can't open that.");
        }
    }

    // Accessors

    public Map<String, GameItem> getContents() {
        return contents;
    }

    public GameItem getContentItem(String name) throws NullPointerException {
        return contents.get(name);
    }

    public boolean areContentsHidden() {
        return contentsHidden;
    }
}