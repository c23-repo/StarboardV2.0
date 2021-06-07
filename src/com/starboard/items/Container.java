package com.starboard.items;

import java.util.Map;
import java.util.HashMap;

public class Container extends GameItem {
    //fields

    // Contents of the container are hidden by default if not specified in constructor.
    private final boolean contentsHidden;
    private final Map<String, GameItem> contents = new HashMap<>();

    // Constructors

    public Container(String name) {
        super(name);
        contentsHidden = true;
    }

    public Container(String name, String description) {
        super(name, description);
        contentsHidden = false;
    }

    public Container(String name, boolean contentsHidden) {
        super(name);
        this.contentsHidden = contentsHidden;
    }

    public Container(String name, String description, boolean contentsHidden) {
        super(name, description);
        this.contentsHidden = contentsHidden;
    }

    // Business

    public GameItem giveItem(String name) throws NullPointerException {
        GameItem item = getContentItem(name);
        contents.remove(name);
        return item;
    }

    // Accessors

    public Map<String, GameItem> getContents() {
        return contents;
    }

    public GameItem getContentItem(String name) throws NullPointerException {
        return contents.get(name);
    }
}