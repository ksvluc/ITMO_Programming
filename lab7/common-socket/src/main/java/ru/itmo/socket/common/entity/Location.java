package ru.itmo.socket.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class Location implements Serializable {
    private String name; //Строка не может быть пустой, Поле не может быть null
    private Integer x; //Поле не может быть null
    private double y;
    private int z;

    public Location(String name, int x, double y, int z) {
        setName(name);
        setX(x);
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {

        return "name: \"" + name + "\"\n" +
                "x: " + x + "\n" +
                "y: " + y + "\n" +
                "z: " + z + "\n";
    }

    public void setName(String name) {
        if (name.trim().equalsIgnoreCase("null") || name.trim().isEmpty())
            throw new IllegalArgumentException("Значение поля name не может быть равно null и не может быть пустым!");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setX(Integer x) {
        if (x == null)
            throw new IllegalArgumentException("Значение поля x не может быть равно null!");
        this.x = x;
    }
    public Integer getX() {
        return x;
    }


    public double getY() {
        return y;
    }


    public int getZ() {
        return z;
    }
}
