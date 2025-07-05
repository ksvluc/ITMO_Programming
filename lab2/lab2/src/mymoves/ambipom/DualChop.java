package mymoves.ambipom;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class DualChop extends PhysicalMove {
    public DualChop(Type type, double pow, double acc, int priority, int hits) {
        super(type, pow, acc, priority, hits);
    }

    @Override
    protected String describe(){
        return "использует Dual Chop";
    }
}
