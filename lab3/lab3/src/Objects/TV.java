package Objects;

import Exceptions.DeviceNotTurnedOnException;
import Interfaces.Watchable;

public class TV implements Watchable {
    private BroadcastEvent currentProgram;
    private boolean isOn;

    public void turnOn() {
        isOn = true;
        System.out.println("Включился телевизор.");
    }

    public void turnOff() {
        isOn = false;
        System.out.println("Телевизор выключился.");
    }

    public void changeProgram(BroadcastEvent program) throws DeviceNotTurnedOnException {
        if (!isOn) {
            throw new DeviceNotTurnedOnException(this);
        }
        currentProgram = program;
        System.out.println("На экране показывают " + program);
    }

    @Override
    public String getProgram() {
        return isOn ? currentProgram.toString() : "Нет программы";
    }
}
