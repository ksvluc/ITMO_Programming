package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class HelpCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        oos.writeUTF(
                "help : вывести справку по доступным командам\n" +
                        "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                        "add : добавить новый элемент в коллекцию\n" +
                        "update_id : обновить значение элемента коллекции, id которого равен заданному\n" +
                        "remove_by_id id : удалить элемент из коллекции по его id\n" +
                        "clear : очистить коллекцию\n" +
                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                        "exit : завершить программу (без сохранения в файл)\n" +
                        "head : вывести первый элемент коллекции и удалить его\n" +
                        "history : вывести последние 6 команд (без их аргументов)\n" +
                        "max_by_creation_date : вывести элемент коллекции с максимальной датой создания\n" +
                        "print_descending : вывести элементы коллекции в порядке убывания\n" +
                        "print_unique_distance : вывести уникальные значения поля distance всех элементов в коллекции\n"
        );
    }
}