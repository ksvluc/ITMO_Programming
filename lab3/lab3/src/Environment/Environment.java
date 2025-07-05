package Environment;

import Entities.Spectator;
import java.util.ArrayList;

public class Environment {
    private ArrayList<Spectator> spectators = new ArrayList<>();
    private String currentEvent;
    private String date;
    public void addSpectator(Spectator spectator) {
        spectators.add(spectator);
    }

    public void setDate(String date) {
        this.date = date;
        System.out.print(date + " ");
    }
}
