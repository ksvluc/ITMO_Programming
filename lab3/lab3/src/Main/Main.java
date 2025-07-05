package Main;

import Entities.*;
import Objects.*;
import Exceptions.*;
import Environment.*;

public class Main {
    public static void main(String[] args){
        try {
            Environment environment = new Environment();

            TV tv = new TV();
            Radio radio = new Radio();

            BroadcastEvent danceShow = new BroadcastEvent("танцы", "Танцующие пары");
            BroadcastEvent officerTale = new BroadcastEvent("рассказ", "об офицере Хныгле");
            Spectator spectator = new Spectator("Лунатики");
            Officer officer = new Officer("Хныгль", new Rifle("50mm", 2000));

            environment.addSpectator(spectator);
            environment.setDate("Сегодня");
            // Сценарий

            tv.turnOn();
            tv.changeProgram(danceShow);
            spectator.watch(tv);
            spectator.setInterests(danceShow, Mood.NOTINTERESTED);


            radio.turnOn();
            radio.changeBroadcast("новости о космосе");
            spectator.listenTo(radio);

            tv.changeProgram(officerTale);
            spectator.setInterests(officerTale, Mood.INTERESTED);
            officer.performAction();
        } catch (DeviceNotTurnedOnException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());

        }
    }
}
