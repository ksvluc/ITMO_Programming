package models;

import utility.Validatable;

public class Location implements Validatable {
    private Integer x; // Поле не может быть null.
    private double y;
    private int z;
    private String name; // Поле не может быть null или пустым.

    public Location(Integer x, double y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public Location(String s) {
        try {
            String[] parts = s.split(";");
            this.x = Integer.parseInt(parts[0]);
            this.y = Double.parseDouble(parts[1]);
            this.z = Integer.parseInt(parts[2]);
            this.name = parts[3];
        } catch (Exception e) {
            // Обработка исключений
        }
    }

    @Override
    public boolean validate() {
        return x != null && name != null && !name.isEmpty();
    }

    @Override
    public String toString() {
        return x + ";" + y + ";" + z + ";" + name;
    }
}
