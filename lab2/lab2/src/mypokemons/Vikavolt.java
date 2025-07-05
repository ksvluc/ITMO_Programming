package mypokemons;

import mymoves.charjabug.IronDefence;
import mymoves.grubbin.PoisonJab;
import mymoves.grubbin.ViseGrip;
import mymoves.vikavolt.Rest;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Vikavolt extends Pokemon {
    public Vikavolt(String name, int level) {
        super(name, level);
        super.setType(Type.BUG, Type.ELECTRIC);
        super.setStats(77,70,90,145,75,43);

        PoisonJab poisonJab = new PoisonJab(Type.POISON, 80, 100);
        ViseGrip viseGrip = new ViseGrip(Type.NORMAL, 55, 100);
        IronDefence ironDefence = new IronDefence(Type.STEEL,0,0);
        Rest rest = new Rest(Type.PSYCHIC,0,0);
        super.setMove(poisonJab, viseGrip, ironDefence, rest);
    }
}
