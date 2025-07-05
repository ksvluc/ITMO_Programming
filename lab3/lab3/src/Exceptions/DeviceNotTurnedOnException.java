package Exceptions;

public class DeviceNotTurnedOnException extends Exception {
    private final Object device;

    public DeviceNotTurnedOnException(Object device) {
        this.device = device;
    }

    @Override
    public String getMessage() {
        String deviceType = device != null ? device.getClass().getSimpleName() : "Неизвестное устройство";
        return "Ошибка устройства (" + deviceType + "): не включен";
    }

}
