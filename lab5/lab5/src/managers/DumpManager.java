package managers;

import au.com.bytecode.opencsv.*;
import models.Route;
import utility.Console;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Класс для управления сохранением и загрузкой коллекции в формате CSV.
 */
public class DumpManager {
    private final String fileName;
    private final Console console;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    /**
     * Преобразует коллекцию в CSV-строку.
     * @param collection Коллекция для преобразования.
     * @return CSV-строка.
     */
    private String collection2CSV(Collection<Route> collection) {
        try {
            StringWriter sw = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(sw, ';');
            for (var e : collection) {
                csvWriter.writeNext(Route.toArray(e));
            }
            return sw.toString();
        } catch (Exception e) {
            console.printError("Ошибка сериализации");
            return null;
        }
    }

    /**
     * Записывает коллекцию в файл.
     * @param collection Коллекция для записи.
     */
    public void writeCollection(Collection<Route> collection) {
        OutputStreamWriter writer = null;
        try {
            var csv = collection2CSV(collection);
            if (csv == null) return;
            writer = new OutputStreamWriter(new FileOutputStream(fileName));
            try {
                writer.write(csv);
                writer.flush();
                console.println("Коллекция успешно сохранена в файл!");
            } catch (IOException e) {
                console.printError("Неожиданная ошибка сохранения");
            }
        } catch (FileNotFoundException | NullPointerException e) {
            console.printError("Файл не найден");
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                console.printError("Ошибка закрытия файла");
            }
        }
    }

    /**
     * Преобразует CSV-строку в коллекцию.
     * @param s CSV-строка.
     * @return Коллекция.
     */
    private LinkedList<Route> CSV2collection(String s) {
        try {
            StringReader sr = new StringReader(s);
            CSVReader csvReader = new CSVReader(sr, ';');
            LinkedList<Route> ds = new LinkedList<>();
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                Route d = Route.fromArray(record);
                if (d != null && d.validate()) {
                    ds.add(d);
                } else {
                    console.printError("Файл с коллекцией содержит недействительные данные");
                }
            }
            csvReader.close();
            return ds;
        } catch (Exception e) {
            console.printError("Ошибка десериализации");
            return null;
        }
    }

    /**
     * Считывает коллекцию из файла.
     * @param collection Коллекция для записи считанных данных.
     */
    public void readCollection(Collection<Route> collection) {
        if (fileName != null && !fileName.isEmpty()) {
            try (var fileReader = new Scanner(new File(fileName))) {
                var s = new StringBuilder();
                while (fileReader.hasNextLine()) {
                    s.append(fileReader.nextLine()).append("\n");
                }
                collection.clear();
                collection.addAll(CSV2collection(s.toString()));
                if (!collection.isEmpty()) {
                    console.println("Коллекция успешно загружена!");
                } else {
                    console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
                }
            } catch (FileNotFoundException exception) {
                console.printError("Загрузочный файл не найден!");
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        } else {
            console.printError("Аргумент командной строки с загрузочным файлом не найден!");
        }
    }
}
