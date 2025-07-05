package mymoves.vikavolt;

import ru.ifmo.se.pokemon.*;

public class Rest extends StatusMove {
    public Rest(Type type, double pow, double acc) {
        super(type, pow, acc);
    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect e = new Effect().condition(Status.SLEEP).turns(2);
        p.addEffect(e);
        p.restore();
    }

    @Override
    protected String describe(){
        return "На чиле, на расслабоне";
    }
}
