package mymoves.drampa;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class AquaTail extends PhysicalMove {
    public AquaTail(Type type, double pow, double acc) {
        super(type, pow, acc);


    }
    @Override
    protected String describe(){
        return "использует Aqua Tail";
    }
}
