package ru.itmo.socket.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class Coordinates implements Serializable {
    private double x; //Значение поля должно быть больше -68
    private int y;

    public Coordinates(double x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    public void setX(double x) {
        if (x <= -68)
            throw new IllegalArgumentException("X должен быть больше -68!");
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }
}
