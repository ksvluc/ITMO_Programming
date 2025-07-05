package Models;

public class Location {
    private Integer x; //Поле не может быть null
    private double y;
    private int z;
    private String name; //Строка не может быть пустой, Поле не может быть null

    public void setX(Integer x) {
        this.x = x;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setY (double y){
        this.y = y;
    }

    public void setZ(int z){
        this.z = z;
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

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return String.format(
                "{name='%s', x=%d, y='%s', z=%dm }",
                name, x, y, z
        );
    }
}