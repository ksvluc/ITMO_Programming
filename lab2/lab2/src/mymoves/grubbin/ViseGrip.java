package mymoves.grubbin;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class ViseGrip extends PhysicalMove {
    public ViseGrip(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected String describe(){
        return "использует Vise Grip";
    }
}
