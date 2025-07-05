package mypokemons;

import mymoves.drampa.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Drampa extends Pokemon {
    public Drampa(String name, int level){
        super(name, level);

        super.setType(Type.NORMAL, Type.DRAGON);
        super.setStats(78, 60, 85, 135, 91, 36);

        AquaTail aquaTail = new AquaTail(Type.WATER,90,90);
        CalmMind calmMind = new CalmMind(Type.PSYCHIC,0,0);
        DoubleTeam doubleTeam = new DoubleTeam(Type.NORMAL,0,0);
        Psychic psychic = new Psychic(Type.PSYCHIC,90,100);
        super.setMove(aquaTail, calmMind, doubleTeam, psychic);
    }
}
