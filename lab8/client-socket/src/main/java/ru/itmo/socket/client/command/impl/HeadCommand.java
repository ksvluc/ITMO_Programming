package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class HeadCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.print("Вы действительно хотите удалить первый элемент коллекции? (y/n): ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("y")) {
            return Optional.of(""); // Пустой объект как подтверждение
        }
        return Optional.empty(); // Отмена операции
    }

}