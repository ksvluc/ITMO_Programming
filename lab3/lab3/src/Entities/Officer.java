package Entities;

import Interfaces.Weapon;

public class Officer extends Person {
    private Weapon weapon;
    private boolean inWeightlessness;
    private boolean weaponFired;

    public Officer(String name, Weapon weapon) {
        super(name, "Полицейский", Mood.CALM);
        this.weapon = weapon;
        this.inWeightlessness = false;
        this.weaponFired = false;
    }

    public void shoot() {
        System.out.println(name + " пытается выстрелить...");
        weaponFired = weapon.fire();
        if (weaponFired) {
            System.out.println(name + " попал в состояние невесомости!");
            this.inWeightlessness = true;
        } else {
            System.out.println(name + " не смог попасть в состояние невесомости, так как винтовка дала осечку.");
        }
    }

    public void fly() {
        if (inWeightlessness && weaponFired) {
            System.out.println(name + " облетает вокруг ядра Луны!");
        } else {
            System.out.println(name + " не может летать без невесомости или выстрела.");
        }
    }

    @Override
    public void performAction() {
        shoot();
        fly();
    }
}
