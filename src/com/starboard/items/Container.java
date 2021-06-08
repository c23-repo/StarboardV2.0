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

    // Accessors

    public Map<String, GameItem> getContents() {
        return contents;
    }

    public GameItem getContentItem(String name) throws NullPointerException {
        return contents.get(name);
    }
}