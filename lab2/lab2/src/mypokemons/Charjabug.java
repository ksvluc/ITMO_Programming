package mypokemons;

import mymoves.charjabug.IronDefence;
import mymoves.grubbin.PoisonJab;
import mymoves.grubbin.ViseGrip;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Charjabug extends Pokemon {
    public Charjabug(String name, int level) {
        super(name, level);
        super.setType(Type.BUG, Type.ELECTRIC);
        super.setStats(57,82,95,55,75,36);

        PoisonJab poisonJab = new PoisonJab(Type.POISON, 80, 100);
        ViseGrip viseGrip = new ViseGrip(Type.NORMAL, 55, 100);
        IronDefence ironDefence = new IronDefence(Type.STEEL,0,0);
        super.setMove(poisonJab, viseGrip, ironDefence);

    }
}
