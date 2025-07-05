package mypokemons;

import mymoves.aipom.Swagger;
import mymoves.aipom.Tickle;
import mymoves.aipom.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Aipom extends Pokemon {
    public Aipom(String name, int level) {

        super(name, level);
        super.setType(Type.NORMAL);
        super.setStats(55,70,55,40,55,85);

        Tickle tickle = new Tickle(Type.NORMAL, 0,100);
        WorkUp workUp = new WorkUp(Type.NORMAL, 0,0);
        Swagger swagger = new Swagger(Type.NORMAL, 0,85);
        super.setMove(tickle, workUp, swagger);

    }
}
