package Objects;

import Interfaces.Weapon;
import java.util.Random;

public class Rifle implements Weapon {
    private String caliber;
    private int range;
    private Random random;

    public Rifle(String caliber, int range) {
        this.caliber = caliber;
        this.range = range;
        this.random = new Random();
    }

    public boolean fire() {
        int chance = random.nextInt(100);
        if (chance < 5) {
            System.out.println("Осечка! Винтовка калибра " + caliber + " не смогла выстрелить...");
            return false;
        } else {
            System.out.println("Винтовка калибра " + caliber + " сделала выстрел!");
            return true;
        }
    }

    @Override
    public String toString() {
        return "Rifle{caliber='" + caliber + "', range=" + range + "}";
    }
}
