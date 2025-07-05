package Data;

import Models.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DataProvider {
    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void save(LinkedList<Route> routes, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileName),
                        StandardCharsets.UTF_8))) {

            writer.write("id,name,distance,creationDate,coordX,coordY,fromX,fromY,fromZ,fromName,toX,toY,toZ,toName");
            writer.newLine();

            for (Route route : routes) {
                writer.write(routeToCsvLine(route));
                writer.newLine();
            }
        }
    }

    public LinkedList<Route> load(String fileName) throws IOException {
        LinkedList<Route> routes = new LinkedList<>();
        Set<Long> idSet = new HashSet<>();

        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + fileName + " не найден");
        }
        if (file.length() == 0) {
            throw new IOException("Файл пуст");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            String header = reader.readLine();
            if (header == null || !header.startsWith("id,name,distance")) {
                throw new IOException("Некорректный формат файла");
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                Route route = parseCsvLine(line);

                if (idSet.contains(route.getId())) {
                    System.out.println("Обнаружен дубликат ID " + route.getId() + " в строке " + lineNumber);
                    break;
                }
                idSet.add(route.getId());

                routes.add(route);
            }
        }
        return routes;
    }

    private String routeToCsvLine(Route route) {
        return String.join(CSV_SEPARATOR,
                String.valueOf(route.getId()),
                (route.getName()),
                String.valueOf(route.getDistance()),
                route.getCreationDate().format(DATE_FORMATTER),
                String.valueOf(route.getCoordinates().getX()),
                String.valueOf(route.getCoordinates().getY()),
                route.getFrom() != null ? String.valueOf(route.getFrom().getX()) : "",
                route.getFrom() != null ? String.valueOf(route.getFrom().getY()) : "",
                route.getFrom() != null ? String.valueOf(route.getFrom().getZ()) : "",
                route.getFrom() != null ? (route.getFrom().getName()) : "",
                route.getTo() != null ? String.valueOf(route.getTo().getX()) : "",
                route.getTo() != null ? String.valueOf(route.getTo().getY()) : "",
                route.getTo() != null ? String.valueOf(route.getTo().getZ()) : "",
                route.getTo() != null ? (route.getTo().getName()) : ""
        );
    }

    private Route parseCsvLine(String line) throws IllegalArgumentException {
        String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (values.length < 14) {
            throw new IllegalArgumentException("Недостаточно данных в строке");
        }

        Route route = new Route();
        try {
            route.setId(Long.parseLong(values[0]));
            route.setName((values[1]));
            route.setDistance(Integer.parseInt(values[2]));

            Coordinates coords = new Coordinates();
            coords.setX(Double.parseDouble(values[4]));
            coords.setY(Integer.parseInt(values[5]));
            route.setCoordinates(coords);

            if (!values[6].isEmpty()) {
                Location from = new Location();
                from.setX(Integer.parseInt(values[6]));
                from.setY(Double.parseDouble(values[7]));
                from.setZ(Integer.parseInt(values[8]));
                from.setName((values[9]));
                route.setFrom(from);
            }

            if (!values[10].isEmpty()) {
                Location to = new Location();
                to.setX(Integer.parseInt(values[10]));
                to.setY(Double.parseDouble(values[11]));
                to.setZ(Integer.parseInt(values[12]));
                to.setName((values[13]));
                route.setTo(to);
            }

            return route;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный числовой формат");
        }
    }
}

