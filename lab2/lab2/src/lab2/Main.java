package lab2;

import mypokemons.*;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        start();
    }
    public static void start() {
        Battle b = new Battle();

        Pokemon drampa = new Drampa("durian",1);
        Pokemon aipom = new Aipom("monkey",15);
        Pokemon ambipom =new Ambipom("gigamonkey",15);
        Pokemon grubbin = new Grubbin("garbagge",1);
        Pokemon charjabug = new Charjabug("shreder",57);
        Pokemon vikavolt = new Vikavolt("sigma",57);

        b.addAlly(aipom);
        b.addAlly(grubbin);
        b.addAlly(charjabug);

        b.addFoe(drampa);
        b.addFoe(ambipom);
        b.addFoe(vikavolt);

        b.go();
    }
}