package Entities;

import Exceptions.InvalidMoodException;

public abstract class Person extends Entity {
    protected Mood mood;

    public Person(String name, String description, Mood mood) {
        super(name, description);
        setMood(mood);
    }

    public void setMood(Mood mood) {
        if (mood == null) {
            throw new InvalidMoodException("Настроение не может быть null!");
        }
        this.mood = mood;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return name.equals(person.name) && description.equals(person.description);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public String toString() {
        return name + " (" + description + ") в настроении " + mood;
    }
}
