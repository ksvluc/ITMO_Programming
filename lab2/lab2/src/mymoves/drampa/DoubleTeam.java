package mymoves.drampa;

import ru.ifmo.se.pokemon.*;

public class DoubleTeam extends StatusMove {
    public DoubleTeam(Type type, double pow, double acc) {
        super(type, pow, acc);

    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect e = new Effect().stat(Stat.EVASION,1);
        p.addEffect(e);
    }
    @Override
    protected String describe(){
        return "использует Double Team";
    }
}
