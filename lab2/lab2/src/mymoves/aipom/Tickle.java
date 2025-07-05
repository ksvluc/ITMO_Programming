package mymoves.aipom;

import ru.ifmo.se.pokemon.*;

public class Tickle extends StatusMove {
    public Tickle(Type type, double pow, double acc) {
        super(type, pow, acc);


    }
    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect e = new Effect().stat(Stat.ATTACK,-1).stat(Stat.DEFENSE,-1);
        p.addEffect(e);
    }
    @Override
    protected String describe(){
        return "использует Tickle";
    }
}
