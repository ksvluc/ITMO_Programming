package mypokemons;

import mymoves.grubbin.PoisonJab;
import mymoves.grubbin.ViseGrip;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Grubbin extends Pokemon {
    public Grubbin(String name, int level) {
        super(name, level);
        super.setType(Type.BUG);
        super.setStats(47,62,45,55,45,46);

        PoisonJab poisonJab = new PoisonJab(Type.POISON, 80, 100);
        ViseGrip viseGrip = new ViseGrip(Type.NORMAL, 55, 100);
        super.setMove(poisonJab, viseGrip);
    }
}
