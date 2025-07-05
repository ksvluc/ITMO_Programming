package Entities;

public abstract class Entity {
    protected String name;
    protected String description;

    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void performAction();
}
