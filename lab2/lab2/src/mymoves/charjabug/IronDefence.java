package mymoves.charjabug;

import ru.ifmo.se.pokemon.*;

public class IronDefence extends StatusMove {
    public IronDefence(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect e = new Effect().stat(Stat.DEFENSE,2);
        p.addEffect(e);
    }
    @Override
    protected String describe(){
        return "использует Iron Defence";
    }
}
