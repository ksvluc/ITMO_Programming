package models;

import java.util.NoSuchElementException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import utility.Console;

public class Ask {
    public static class AskBreak extends Exception {}

    public static Route askRoute(Console console, long id) throws AskBreak {
        try {
            String name;
            while (true) {
                console.print("name: ");
                name = console.readln().trim();
                if (name.equals("exit")) throw new AskBreak();
                if (!name.isEmpty()) break;
            }
            var coordinates = askCoordinates(console);
            var from = askLocation(console, "from");
            var to = askLocation(console, "to");
            int distance;
            while (true) {
                console.print("distance: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        distance = Integer.parseInt(line);
                        if (distance > 1) break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное значение для distance (целое число > 1).");
                    }
                }
            }
            return new Route(id, name, coordinates, from, to, distance);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Coordinates askCoordinates(Console console) throws AskBreak {
        try {
            double x;
            while (true) {
                console.print("coordinates.x: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        x = Double.parseDouble(line);
                        if (x > -68) break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное значение для coordinates.x (double > -68).");
                    }
                }
            }
            int y;
            while (true) {
                console.print("coordinates.y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        y = Integer.parseInt(line); break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное значение для coordinates.y (целое число).");
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Location askLocation(Console console, String promptPrefix) throws AskBreak {
        try {
            Integer x;
            while (true) {
                console.print(promptPrefix + ".x: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        x = Integer.parseInt(line);
                        if (x != null) break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное значение для " + promptPrefix + ".x (целое число, не может быть null).");
                    }
                }
            }
            double y;
            while (true) {
                console.print(promptPrefix + ".y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        y = Double.parseDouble(line); break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное значение для " + promptPrefix + ".y (double).");
                    }
                }
            }
            int z;
            while (true) {
                console.print(promptPrefix + ".z: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        z = Integer.parseInt(line); break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное значение для " + promptPrefix + ".z (целое число).");
                    }
                }
            }
            String name;
            while (true) {
                console.print(promptPrefix + ".name: ");
                name = console.readln().trim();
                if (name.equals("exit")) throw new AskBreak();
                if (!name.isEmpty()) break;
            }
            return new Location(x, y, z, name);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}
