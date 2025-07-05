package Entities;

public enum Mood {
    INTERESTED, CALM, NOTINTERESTED, NEUTRAL;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
