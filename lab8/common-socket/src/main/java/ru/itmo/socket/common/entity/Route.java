package ru.itmo.socket.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Route implements Comparable<Route>, Serializable {
    private static long lastGeneratedId = 2;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле может быть null
    private Location to; //Поле может быть null
    private int distance; //Значение поля должно быть больше 1
    private long creatorId;


    public Route(String name, Coordinates coordinates, Location from, Location to, int distance) {
        this.id = -1; // Автоматическая генерация id на сервере в БД
        setName(name);
        this.creationDate = LocalDateTime.now();
        setCoordinates(coordinates);
        this.from = from;
        this.to = to;
        setDistance(distance);
    }

    public static long generateId() {
        return ++lastGeneratedId;
    }

    @Override
    public String toString() {

        String fromStr = (from != null) ? from.toString() : "Не указана";
        String toStr = (this.to != null) ? this.to.toString() : "Не указана";

        return "\n" +
                "id: " + id + "\n" +
                "name: \"" + name + "\"\n" +
                "coordinates: " + coordinates + "\n" +
                "created: " + creationDate + "\n" +
                "from: " + fromStr + "\n" +
                "to: " + toStr + "\n" +
                "distance: " + distance;
    }


    @Override
    public int compareTo(Route route) {
        return Long.compare(this.id, route.id);
    }


    public void setName(String name) {
        if (name.trim().equalsIgnoreCase("null") || name.trim().isEmpty())
            throw new IllegalArgumentException("Значение поля name не может быть равно null и не может быть пустым!");
        this.name = name;
    }


    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null)
            throw new IllegalArgumentException("Значение поля coordinates не может быть равно null!");
        this.coordinates = coordinates;
    }



    public void setDistance(int distance) {
        if (distance < 1)
            throw new IllegalArgumentException("Значение поля minimalPoint должно быть больше или равно 1!");
        this.distance = distance;
    }


}

