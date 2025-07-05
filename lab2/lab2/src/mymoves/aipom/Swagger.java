package mymoves.aipom;

import ru.ifmo.se.pokemon.*;

public class Swagger extends StatusMove {
    public Swagger(Type type, double pow, double acc) {
        super(type, pow, acc);


    }
    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect e = new Effect().stat(Stat.ATTACK,2);
        Effect.confuse(p);
        p.addEffect(e);

    }
    @Override
    protected String describe(){
        return "использует Swagger";
    }
}
