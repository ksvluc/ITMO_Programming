package mymoves.grubbin;

import ru.ifmo.se.pokemon.*;

public class PoisonJab extends PhysicalMove {
    public PoisonJab(Type type, double pow, double acc) {
        super(type, pow, acc);

    }
    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect e = new Effect().chance(0.3).condition(Status.POISON);
        p.addEffect(e);
    }

    @Override
    protected String describe(){
        return "использует Poison Jab";
    }
}
