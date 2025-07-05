package Entities;

import Interfaces.Watchable;
import Interfaces.ListenTo;
import Objects.BroadcastEvent;

public class Spectator extends Person {
    public Spectator(String name) {
        super(name, "Зритель", Mood.NEUTRAL);
    }

    public void watch(Watchable watchable) {
        System.out.println(name + " смотрят " + watchable.getProgram());
    }

    public void setInterests(BroadcastEvent program, Mood mood) {
        if (mood == Mood.NOTINTERESTED) {
            System.out.println(name + " не заинтересованы в " + program);
        }
        else if (mood == Mood.INTERESTED) {
            System.out.println(name + " заинтересованы в " + program.name() + "e " + program.description());
        }
    }
    public void listenTo(ListenTo listenTo) {
        System.out.println(name + " слушают " + listenTo.getBroadcast());
    }

    @Override
    public void performAction() {
        System.out.println(name + " ожидают интересного события.");
    }

}
