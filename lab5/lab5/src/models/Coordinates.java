package models;

import utility.Validatable;

public class Coordinates implements Validatable {
    private double x; // Значение должно быть больше -68.
    private int y;

    public Coordinates(double x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(String s) {
        try {
            String[] parts = s.split(";");
            this.x = Double.parseDouble(parts[0]);
            this.y = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            // Обработка исключений
        }
    }

    @Override
    public boolean validate() {
        return x > -68;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }
}
