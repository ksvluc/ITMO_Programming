package Models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Route implements Serializable, Comparable<Route> {
    private static long idCounter = 1;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле может быть null
    private Location to; //Поле может быть null
    private int distance; //Значение поля должно быть больше 1

    public Route() {
        this.id = idCounter++;
        this.creationDate = LocalDateTime.now();
    }
    public void setId(long id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setCoordinates(Coordinates coordinates){
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        this.coordinates = coordinates;
    }

    public void setFrom (Location from){
        this.from = from;
    }
    public void setTo (Location to){
        this.to = to;
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    public Location getFrom(){
        return from;
    }

    public Location getTo(){
        return to;
    }

    public int getDistance(){
        return distance;
    }
    @Override
    public String toString() {
        return String.format(
                "Route{id=%d, name='%s', distance=%d, creationDate=%s, coordinates=%s, from=%s, to=%s}",
                id, name, distance, creationDate, coordinates, from, to
        );
    }

    @Override
    public int compareTo(Route other) {
        return Long.compare(this.id, other.id);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Route route = (Route) obj;
        return id == route.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
