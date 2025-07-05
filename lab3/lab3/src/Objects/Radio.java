package Objects;

import Exceptions.DeviceNotTurnedOnException;
import Interfaces.ListenTo;

public class Radio implements ListenTo {
    private String currentBroadcast;
    private boolean isOn;

    public void turnOn() {
        isOn = true;
        System.out.println("Включилось радио.");
    }

    public void turnOff() {
        isOn = false;
        System.out.println("Радио выключилось.");
    }

    public void changeBroadcast(String broadcast) throws DeviceNotTurnedOnException {
        if (!isOn) {
            throw new DeviceNotTurnedOnException(this);
        }
        currentBroadcast = broadcast;
        System.out.println("Радио передает " + broadcast);
    }

    @Override
    public String getBroadcast() {
        return isOn ? currentBroadcast : "Нет передачи";
    }
}
