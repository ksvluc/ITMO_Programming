package utility;

/**
 * Класс для представления результата выполнения команды.
 */
public class ExecutionResponse {
    private final boolean exitCode;
    private final String message;

    public ExecutionResponse(boolean code, String message) {
        this.exitCode = code;
        this.message = message;
    }

    public ExecutionResponse(String message) {
        this(true, message);
    }

    public boolean getExitCode() {
        return exitCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ExecutionResponse{" +
                "exitCode=" + exitCode +
                ", message='" + message + '\'' +
                '}';
    }
}
