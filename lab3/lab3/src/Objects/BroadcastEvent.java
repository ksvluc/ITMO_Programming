package Objects;

public record BroadcastEvent(String name, String description) {
    @Override
    public String toString() {
        return name + " " + description;
    }
}
