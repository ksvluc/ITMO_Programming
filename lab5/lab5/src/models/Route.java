package models;

import utility.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Класс Route, хранящий основную информацию.
 */
public class Route extends Element {
    private long id; // Значение должно быть уникальным и больше 0.
    private String name; // Поле не может быть null или пустым.
    private Coordinates coordinates; // Поле не может быть null.
    private LocalDateTime creationDate; // Генерируется автоматически.
    private Location from; // Может быть null.
    private Location to; // Может быть null.
    private int distance; // Должно быть больше 1.

    public Route(long id, String name, Coordinates coordinates, Location from, Location to, int distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    @Override
    public boolean validate() {
        return id > 0 && name != null && !name.isEmpty() &&
                coordinates != null && coordinates.validate() &&
                distance > 1;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public int getDistance() {
        return distance;
    }

    public static Route fromArray(String[] a) {
        long id;
        String name;
        LocalDateTime creationDate;
        Coordinates coordinates;
        Location from = null;
        Location to = null;
        int distance;
        try {
            id = Long.parseLong(a[0]);
            name = a[1];
            creationDate = LocalDateTime.parse(a[2], DateTimeFormatter.ISO_DATE_TIME);
            coordinates = new Coordinates(a[3]);
            if (!a[4].equals("null")) from = new Location(a[4]);
            if (!a[5].equals("null")) to = new Location(a[5]);
            distance = Integer.parseInt(a[6]);
            return new Route(id, name, coordinates, from, to, distance);
        } catch (Exception e) {
            return null;
        }
    }

    public static String[] toArray(Route e) {
        var list = new ArrayList<String>();
        list.add(Long.toString(e.id));
        list.add(e.name);
        list.add(e.creationDate.format(DateTimeFormatter.ISO_DATE_TIME));
        list.add(e.coordinates.toString());
        list.add(e.from == null ? "null" : e.from.toString());
        list.add(e.to == null ? "null" : e.to.toString());
        list.add(Integer.toString(e.distance));
        return list.toArray(new String[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route that = (Route) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creationDate, coordinates, from, to, distance);
    }

    @Override
    public String toString() {
        return String.format(
                "Route{id=%d, name='%s', creationDate='%s', coordinates=%s, from=%s, to=%s, distance=%d}",
                id, name, creationDate, coordinates, from, to, distance
        );
    }
}

