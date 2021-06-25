package com.starboard.items;

public abstract class GameItem implements Cloneable {
    // Attributes

    protected String name;
    protected boolean isPortable;
    protected int damage;
    protected int baseDamage;
    private String description;
    private int quantity;
    private double weight;
    private boolean needsAmmo;
    private int maxAmmo;
    private int totalAmmo;


    // Business

    public GameItem() {
    }

    public GameItem(String name) {
        this.name = name;
    }


    // Constructors

    public GameItem(String name, String description) {
        this(name);
        this.description = description;
    }

    public GameItem(String name, boolean isPortable) {
        this.name = name;
        this.isPortable = isPortable;
    }

    public GameItem(String name, boolean isPortable, String description) {
        this(name, isPortable);
        this.description = description;
    }

    public GameItem(String name, boolean isPortable, int quantity) {
        this(name, isPortable);
        this.quantity = quantity;
    }

    public GameItem(String name, boolean isPortable, String description, int quantity) {
        this(name, isPortable, description);
        this.quantity = quantity;
    }

    public GameItem(String name, String description, boolean isPortable, int quantity, double weight) {
        this(name, isPortable, description, quantity);
        this.weight = weight;
    }

    public GameItem(String name, String description, boolean isPortable, int quantity, double weight, boolean needsAmmo, int maxAmmo) {
        this(name, description, isPortable, quantity, weight);
        this.needsAmmo = needsAmmo;
        this.maxAmmo = maxAmmo;
    }

    public GameItem(String name, String description, boolean isPortable, int quantity, double weight, boolean needsAmmo, int maxAmmo, int totalAmmo) {
        this(name, description, isPortable, quantity, weight, needsAmmo, maxAmmo);
        this.totalAmmo = totalAmmo;
    }

    public <T extends GameItem> T cloneToType() {
        try {
            return (T) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Accessors

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPortable() {
        return isPortable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changeQuantity(int change) {
        this.quantity += change;
    }

    public double getWeight() {
        return weight * quantity;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isNeedsAmmo() {
        return needsAmmo;
    }

    public void setNeedsAmmo(boolean needsAmmo) {
        this.needsAmmo = needsAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public int getTotalAmmo() {
        return totalAmmo;
    }

    public void setTotalAmmo(int moreAmmo) {
        this.totalAmmo = moreAmmo;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }
}