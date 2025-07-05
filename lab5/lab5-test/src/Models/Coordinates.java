package Models;

public class Coordinates {
    private double x; //Значение поля должно быть больше -68
    private int y;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
    public double getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    @Override
    public String toString() {
        return String.format(
                "{x='%s', y=%d}",
                x, y
        );
    }



}

