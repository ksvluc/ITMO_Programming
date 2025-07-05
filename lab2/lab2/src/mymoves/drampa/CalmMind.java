package mymoves.drampa;

import ru.ifmo.se.pokemon.*;

public class CalmMind extends StatusMove {
    public CalmMind(Type type, double pow, double acc) {
        super(type, pow, acc);

    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect e = new Effect().stat(Stat.SPECIAL_ATTACK,1).stat(Stat.SPECIAL_DEFENSE,1);
        p.addEffect(e);
    }

    @Override
    protected String describe(){
        return "использует Calm Mind";
    }
}
