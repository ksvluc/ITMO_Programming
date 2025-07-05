package mypokemons;

import mymoves.aipom.Swagger;
import mymoves.aipom.Tickle;
import mymoves.aipom.WorkUp;
import mymoves.ambipom.DualChop;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Ambipom extends Pokemon {
    public Ambipom(String name, int level) {
        super(name, level);
        super.setType(Type.NORMAL);
        super.setStats(75,100,66,60,66,115);

        Tickle tickle = new Tickle(Type.NORMAL, 0,100);
        WorkUp workUp = new WorkUp(Type.NORMAL, 0,0);
        Swagger swagger = new Swagger(Type.NORMAL, 0,85);
        DualChop dualChop = new DualChop(Type.DRAGON, 40,90,0,2);
        super.setMove(tickle, workUp, swagger, dualChop);
    }
}
