package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.common.entity.Location; // Исправленный импорт

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.TreeSet;

/**
 * Команда для сохранения коллекции Route в XML-файл.
 */
public class SaveCommand implements ServerCommand {

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        String fileName = String.valueOf(args[0]);
        TreeSet<Route> routes = RouteTreeSetManager.getInstance().getAllElements();
        if (routes.isEmpty()) {
            oos.writeUTF("Коллекция пуста. Нечего сохранять.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<Routes>\n");
            for (Route rt : routes) {
                writer.write("  <Route>\n");
                writer.write("    <id>" + rt.getId() + "</id>\n");
                writer.write("    <name>" + escapeXml(rt.getName()) + "</name>\n");
                writer.write("    <coordinates>\n");
                writer.write("      <x>" + rt.getCoordinates().getX() + "</x>\n");
                writer.write("      <y>" + rt.getCoordinates().getY() + "</y>\n");
                writer.write("    </coordinates>\n");
                writer.write("    <creationDate>" + rt.getCreationDate() + "</creationDate>\n");

                // Обработка локации from
                Location from = rt.getFrom();
                if (from != null) {
                    writer.write("    <from>\n");
                    writer.write("      <name>" + escapeXml(from.getName()) + "</name>\n");
                    writer.write("      <x>" + from.getX() + "</x>\n");
                    writer.write("      <y>" + from.getY() + "</y>\n");
                    writer.write("      <z>" + from.getZ() + "</z>\n");
                    writer.write("    </from>\n");
                }

                // Обработка локации to
                Location to = rt.getTo();
                if (to != null) {
                    writer.write("    <to>\n");
                    writer.write("      <name>" + escapeXml(to.getName()) + "</name>\n");
                    writer.write("      <x>" + to.getX() + "</x>\n");
                    writer.write("      <y>" + to.getY() + "</y>\n");
                    writer.write("      <z>" + to.getZ() + "</z>\n");
                    writer.write("    </to>\n");
                }
                writer.write("    <distance>" + rt.getDistance() + "</distance>\n");

                writer.write("  </Route>\n");
            }
            writer.write("</Routes>\n");
            oos.writeUTF("Коллекция успешно сохранена в файл: " + fileName);
        } catch (IOException e) {
            oos.writeUTF("Ошибка при сохранении коллекции: " + e.getMessage());
        }
    }

    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}