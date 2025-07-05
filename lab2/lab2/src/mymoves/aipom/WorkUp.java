package mymoves.aipom;

import ru.ifmo.se.pokemon.*;

public class WorkUp extends StatusMove{
    public WorkUp(Type type, double pow, double acc) {
        super(type, pow, acc);


    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect e = new Effect().stat(Stat.ATTACK,1).stat(Stat.SPECIAL_ATTACK,1);
        p.addEffect(e);
    }
    @Override
    protected String describe(){
        return "использует Work Up";
    }
}
